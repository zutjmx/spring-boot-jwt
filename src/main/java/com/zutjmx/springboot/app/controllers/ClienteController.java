package com.zutjmx.springboot.app.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
//import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//import com.zutjmx.springboot.app.models.dao.IClienteDao;
import com.zutjmx.springboot.app.models.entity.Cliente;
import com.zutjmx.springboot.app.models.service.IClienteService;
import com.zutjmx.springboot.app.models.service.IUploadFileService;
import com.zutjmx.springboot.app.util.paginator.PageRender;

@Controller
@SessionAttributes("cliente")
public class ClienteController {
	
	@Autowired
	//@Qualifier("clienteDaoJPA")
	private IClienteService clienteService;
		
	@Autowired
	private IUploadFileService uploadFileService;
	
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
	
	@GetMapping(value="/ver/{id}")
	public String ver(@PathVariable(value="id") Long id, Map<String, Object> modelo, RedirectAttributes flash) {
		
		Cliente cliente = clienteService.findOne(id);
		
		if (cliente == null) {
			flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
			return "redirect:/listar";
		}
		
		modelo.put("cliente", cliente);
		modelo.put("titulo", "Detalle del cliente: " + cliente.getNombre());
		
		return "ver";
	}
	
	@RequestMapping(value="/listar",method=RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue="0") int page,Model modelo) {
		
		Pageable pageRequest = PageRequest.of(page, 4);
		
		Page<Cliente> clientes = clienteService.findAll(pageRequest);		
		PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);
		
		modelo.addAttribute("titulo", "Listado de clientes");
		modelo.addAttribute("clientes", clientes);
		modelo.addAttribute("page", pageRender);
		return "listar";
	}
	
	@RequestMapping(value="/formulario-cliente")
	public String crear(Map<String, Object> modelo) {
		Cliente cliente = new Cliente();
		modelo.put("cliente", cliente);
		modelo.put("titulo", "Formulario de Cliente");
		return "formulario-cliente";
	}
	
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

}
