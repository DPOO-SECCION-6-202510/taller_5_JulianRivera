package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import uniandes.dpoo.hamburguesas.mundo.Combo;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class ComboTest {
	
    private ArrayList<ProductoMenu> itemsCombo;
    private ProductoMenu producto1;
    private ProductoMenu producto2;
    private ProductoMenu producto3;
	private Combo combo;
	
	@BeforeEach
	public void setUp() throws Exception
	{
		
	    producto1 = new ProductoMenu("Corral", 14000);
	    producto2 = new ProductoMenu("Papas Medianas", 5500);
	    producto3 = new ProductoMenu("Gaseosa", 5000);
	        
	    itemsCombo = new ArrayList<>();
	    itemsCombo.add(producto1);
	    itemsCombo.add(producto2);
	    itemsCombo.add(producto3);
		combo = new Combo("combo corral", 0.10, itemsCombo);
	}
	
	@AfterEach
	public void tearDown() throws Exception
	{
	}
	
	@Test
	public void testGetNombre()
	{
		assertEquals("combo corral", combo.getNombre(), "El nombre del combo no es el esperado.");
	}
	
	@Test
	public void testGetPrecio()
	{
		ArrayList<ProductoMenu> items = new ArrayList<>();
		items.add(producto1);
		items.add(producto2);
		items.add(producto3);
		int precioEsperado = (int)((14000+5500+5000)*0.9);
		assertEquals(precioEsperado, combo.getPrecio(), "El precio del combo no es el esperado.");
	}
	
	@Test
	public void testGenerarTextoFactura()
	{
        StringBuffer sb = new StringBuffer( );
        sb.append( "Combo: " + "combo corral" + "\n" );
        sb.append( "Descuento: " + 0.10 + "\n" );
        sb.append( "Precio: " + (int)((14000+5500+5000)*0.9) + "\n" );

        assertEquals(sb.toString(), combo.generarTextoFactura(), "El texto de factura no es el esperado.");
	}
}
