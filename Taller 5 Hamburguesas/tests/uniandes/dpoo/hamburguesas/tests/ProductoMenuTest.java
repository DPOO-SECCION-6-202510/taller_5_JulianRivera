package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class ProductoMenuTest
{
	private ProductoMenu producto1;
	
	@BeforeEach
	void setUp() throws Exception
	{
		producto1 = new ProductoMenu("especial" , 24000);
	}
	
	@AfterEach
	void tearDown() throws Exception
	{	
	}
	
	@Test
	void testGetNombre()
	{
		assertEquals("especial", producto1.getNombre(), "El nombre del producto no es el esperado.");
	}
	
	@Test
	void testGetPrecio()
	{
		assertEquals(24000, producto1.getPrecio(), "El precio del producto no coincide con el del menu.");
	}
	
	@Test
	void testGenerarTextoFactura()
	{
        StringBuffer sb = new StringBuffer( );
		sb.append( "especial" + "\n" );
        sb.append( "Precio: " + 24000 + "\n" );
		assertEquals(sb.toString(), producto1.generarTextoFactura(), "El texto de factura generado no es correcto.");
	}
}
