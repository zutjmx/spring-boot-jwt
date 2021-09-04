package com.zutjmx.springboot.app.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
//import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
//import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//import com.zutjmx.springboot.app.models.dao.IClienteDao;
import com.zutjmx.springboot.app.models.entity.Cliente;
import com.zutjmx.springboot.app.models.service.IClienteService;
import com.zutjmx.springboot.app.models.service.IUploadFileService;
import com.zutjmx.springboot.app.util.paginator.PageRender;
import com.zutjmx.springboot.app.view.xml.ClienteList;

@Controller
@SessionAttributes("cliente")
public class ClienteController {
	
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	//@Qualifier("clienteDaoJPA")
	private IClienteService clienteService;
		
	@Autowired
	private IUploadFileService uploadFileService;
	
	@Autowired
	private MessageSource messageSource;
	
	@Secured("ROLE_USER")
	@GetMapping(value="/uploads/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String filename) {
		
		Resource recurso = null;
		try {
			recurso = uploadFileService.load(filename);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		return ResponseEntity
				.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment filename=\"" + recurso.getFilename() + "\"")
				.body(recurso);
		
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping(value="/ver/{id}")
	public String ver(@PathVariable(value="id") Long id, Map<String, Object> modelo, RedirectAttributes flash) {
		
		//Cliente cliente = clienteService.findOne(id);
		Cliente cliente = clienteService.fetchClienteByIdWithFacturas(id);
		
		if (cliente == null) {
			flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
			return "redirect:/listar";
		}
		
		modelo.put("cliente", cliente);
		modelo.put("titulo", "Detalle del cliente: " + cliente.getNombre());
		
		return "ver";
	}
	
	@GetMapping(value="/listar-rest")
	public @ResponseBody ClienteList listarRest() {
		return new ClienteList(clienteService.findAll());
	}
	
	@RequestMapping(value= {"/listar","/"},method=RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue="0") int page, 
						 Model modelo, 
						 Authentication authentication,
						 HttpServletRequest request,
						 Locale locale) {
		
		if (authentication != null) {
			logger.info("Usuario autenticado con authentication :: ".concat(authentication.getName()));
		}
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if (auth != null) {
			logger.info("Usuario autenticado con auth (forma estático) :: ".concat(auth.getName()));
		}
		
		if (hasRole("ROLE_ADMIN")) {
			logger.info(":: Hola ".concat(auth.getName()).concat(", tienes acceso ::"));
		} else {
			logger.info("<> Hola ".concat(auth.getName()).concat(", no tienes acceso <>"));
		}
		
		SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request, "ROLE_");
		
		if (securityContext.isUserInRole("ADMIN")) {
			logger.info("SecurityContextHolderAwareRequestWrapper :: Hola ".concat(auth.getName()).concat(", tienes acceso :: SecurityContextHolderAwareRequestWrapper"));
		} else {
			logger.info("SecurityContextHolderAwareRequestWrapper <> Hola ".concat(auth.getName()).concat(", no tienes acceso <> SecurityContextHolderAwareRequestWrapper"));
		}
		
		if (request.isUserInRole("ROLE_ADMIN")) {
			logger.info("HttpServletRequest :: Hola ".concat(auth.getName()).concat(", tienes acceso :: HttpServletRequest"));
		} else {
			logger.info("HttpServletRequest <> Hola ".concat(auth.getName()).concat(", no tienes acceso <> HttpServletRequest"));
		}
		
		Pageable pageRequest = PageRequest.of(page, 4);
		
		Page<Cliente> clientes = clienteService.findAll(pageRequest);		
		PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);
		
		modelo.addAttribute("titulo", messageSource.getMessage("text.cliente.listar.titulo", null, locale));
		modelo.addAttribute("clientes", clientes);
		modelo.addAttribute("page", pageRender);
		return "listar";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value="/formulario-cliente")
	public String crear(Map<String, Object> modelo) {
		Cliente cliente = new Cliente();
		modelo.put("cliente", cliente);
		modelo.put("titulo", "Formulario de Cliente");
		return "formulario-cliente";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value="/formulario-cliente/{id}")
	public String editar(@PathVariable(value="id") Long id, Map<String, Object> modelo, RedirectAttributes flash) {
		
		Cliente cliente = null;
		
		if (id > 0) {
			cliente = clienteService.findOne(id);
			if (cliente == null) {
				flash.addFlashAttribute("success", "El id del cliente no existe en la base de datos.");
				return "redirect:/listar";
			}
		} else {
			flash.addFlashAttribute("success", "El id del cliente no puede ser cero.");
			return "redirect:/listar";
		}
		modelo.put("cliente", cliente);
		modelo.put("titulo", "Editar Cliente");
		return "formulario-cliente";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value="/formulario-cliente",method=RequestMethod.POST)
	public String guardar(@Valid Cliente cliente, BindingResult resultado, Model modelo, @RequestParam("file") MultipartFile foto, RedirectAttributes flash, SessionStatus status) {
		
		if (resultado.hasErrors()) {
			modelo.addAttribute("titulo", "Formulario de Cliente");
			return "formulario-cliente";
		}
		
		if (!foto.isEmpty()) {
			
			if (cliente.getId() != null 
					&& cliente.getId() > 0 
					&& cliente.getFoto() != null
					&& cliente.getFoto().length() > 0) {				
				uploadFileService.delete(cliente.getFoto());				
			}
			
			String uniqueFileName = null;
			
			try {
				uniqueFileName = uploadFileService.copy(foto);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			flash.addFlashAttribute("info", "Has subido correctamente '" + uniqueFileName + "'");			
			cliente.setFoto(uniqueFileName);

			
		}
		
		String mensajeFlash = (cliente.getId() != null)? "Cliente editado de forma exitosa." : "Cliente creado de forma exitosa.";
		
		clienteService.save(cliente);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:listar";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value="/eliminar/{id}")
	public String eliminar(@PathVariable(value="id") Long id, RedirectAttributes flash) {		
		if (id > 0) {
			Cliente cliente = clienteService.findOne(id);
			
			clienteService.delete(id);
			flash.addFlashAttribute("success", "Cliente eliminado de forma exitosa.");
			
			if (uploadFileService.delete(cliente.getFoto())) {
				flash.addFlashAttribute("info", "El archivo de imagen " + cliente.getFoto() + " se eliminó con éxito.");
			}
			
		}		
		return "redirect:/listar";
	}
	
	private boolean hasRole(String role) {
		SecurityContext context = SecurityContextHolder.getContext();
		
		if (context == null) {
			return false;
		}
		
		Authentication auth = context.getAuthentication();
		
		if (auth == null) {
			return false;
		}
		
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		
		return authorities.contains(new SimpleGrantedAuthority(role));
		
		/*for (GrantedAuthority grantedAuthority : authorities) {
			if (role.equals(grantedAuthority.getAuthority())) {
				logger.info("== El usuario ".concat(auth.getName()).concat(", tiene el ROLE: ").concat(grantedAuthority.getAuthority()).concat(" =="));
				return true;
			}
		}
		
		return false;*/
		
	}

}
