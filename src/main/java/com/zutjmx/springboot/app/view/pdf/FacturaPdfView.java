package com.zutjmx.springboot.app.view.pdf;

import java.awt.Color;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.lowagie.text.Document;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.zutjmx.springboot.app.models.entity.Factura;
import com.zutjmx.springboot.app.models.entity.ItemFactura;

@Component("factura/ver")
public class FacturaPdfView extends AbstractPdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Factura factura = (Factura) model.get("factura");
		
		PdfPTable tablaCliente = new PdfPTable(1);
		tablaCliente.setSpacingAfter(20);
		
		PdfPCell cell = null;
		
		cell = new PdfPCell(new Phrase("Datos del cliente"));
		cell.setBackgroundColor(new Color(184,218,255));
		cell.setPadding(8f);
		tablaCliente.addCell(cell);
		
		tablaCliente.addCell(factura.getCliente().getNombre().concat(" ").concat(factura.getCliente().getApellido()));
		tablaCliente.addCell(factura.getCliente().getEmail());
		
		PdfPTable tablaFactura = new PdfPTable(1);
		tablaFactura.setSpacingAfter(20);
		
		cell = new PdfPCell(new Phrase("Datos de la factura"));
		cell.setBackgroundColor(new Color(195,230,203));
		cell.setPadding(8f);
		tablaFactura.addCell(cell);
		
		tablaFactura.addCell("Folio: ".concat(factura.getId().toString()));
		tablaFactura.addCell("Descripción: ".concat(factura.getDescripcion()));
		tablaFactura.addCell("Fecha: ".concat(factura.getFechaCreacion().toString()));
		
		document.add(tablaCliente);
		document.add(tablaFactura);
		
		PdfPTable tablaFacturaDetalle = new PdfPTable(4);
		tablaFacturaDetalle.setWidths(new float[]{3.5f,1,1,1});
		tablaFacturaDetalle.addCell("Producto");
		tablaFacturaDetalle.addCell("Precio");
		tablaFacturaDetalle.addCell("Cantidad");
		tablaFacturaDetalle.addCell("Total");
		
		for (ItemFactura item : factura.getItems()) {
			tablaFacturaDetalle.addCell(item.getProducto().getNombre());
			tablaFacturaDetalle.addCell(item.getProducto().getPrecio().toString());
			
			cell = new PdfPCell(new Phrase(item.getCantidad().toString()));
			cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			tablaFacturaDetalle.addCell(cell);
			
			tablaFacturaDetalle.addCell(item.calcularTotal().toString());
		}
		
		cell = new PdfPCell(new Phrase("Total: "));
		cell.setColspan(3);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		tablaFacturaDetalle.addCell(cell);
		tablaFacturaDetalle.addCell(factura.getTotal().toString());
		
		document.add(tablaFacturaDetalle);
	}

}
