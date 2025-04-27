package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;
import uniandes.dpoo.hamburguesas.mundo.Ingrediente;
import uniandes.dpoo.hamburguesas.mundo.ProductoAjustado;

public class ProductoAjustadoTest
{
	private ProductoMenu productoBase;
	private ProductoAjustado productoAjustado;
	private Ingrediente ingrediente;
	
	@BeforeEach
	void setUp()
	{
		productoBase = new ProductoMenu("especial",24000);
		productoAjustado = new ProductoAjustado(productoBase);
	}
	
	@AfterEach
	void tearDown() throws Exception
	{
	}
	
	@Test
	void testGetNombre()
	{
		assertEquals("especial", productoAjustado.getNombre(), "El nombre del producto no es el esperado.");
	}
	
	@Test
	void testGetPrecio()
	{
		ingrediente = new Ingrediente("tomate", 1000);
		productoAjustado.getAgregados().add(ingrediente);
		int precioFinal = 24000 + 1000;
		assertEquals(precioFinal, productoAjustado.getPrecio(), "El precio del producto no es el esperado.");
		
	}
	
	@Test
	void testGenerarTextoFactura()
	{
		Ingrediente ingrediente1 = new Ingrediente("tomate", 1000);
		productoAjustado.getAgregados().add(ingrediente1);
		Ingrediente ingrediente2 = new Ingrediente("cebolla", 1000);
		productoAjustado.getEliminados().add(ingrediente2);
		
        StringBuffer sb = new StringBuffer( );
        sb.append( productoAjustado.getProductoBase() );
        for( Ingrediente ing : productoAjustado.getAgregados() )
        {
            sb.append( "Nombre ingrediente adicional: " + ing.getNombre( ) + "\n");
            sb.append( "Precio ingrediente adicional: " + ing.getCostoAdicional( ) + "\n");
        }
        for( Ingrediente ing : productoAjustado.getEliminados() )
        {
            sb.append( "Nombre ingrediente eliminado: " + ing.getNombre( ) + "\n");
            sb.append( "Precio ingrediente eliminado: " + ing.getCostoAdicional( ) + "\n");
        }
        
        sb.append("Total: " + 25000 + "\n");
        assertEquals(sb.toString(), productoAjustado.generarTextoFactura(), "El texto de la factura no es el esperado.");
        System.out.println();
	}
	
}
