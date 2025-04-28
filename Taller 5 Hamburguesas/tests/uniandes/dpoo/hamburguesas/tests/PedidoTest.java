package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Combo;
import uniandes.dpoo.hamburguesas.mundo.Pedido;
import uniandes.dpoo.hamburguesas.mundo.Producto;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class PedidoTest 
{
	private Pedido pedido;
	private ProductoMenu producto1;
	private ProductoMenu producto2;
	
	@BeforeEach
	public void setUp() throws Exception
	{
		pedido = new Pedido("Pepe", "Localidad de Kennedy");
        producto1 = new ProductoMenu("Hamburguesa", 10000);
        producto2 = new ProductoMenu("Papas", 5000);
	}
	
	@AfterEach
	public void tearDown() throws Exception
	{
	}
	
	@Test
	public void testGetIdPedido()
	{
		Pedido nuevoPedido = new Pedido("Maria", "Localidad de Kennedy");
		assertNotEquals(pedido.getIdPedido(), nuevoPedido.getIdPedido(), "El numero de los IDs no deben ser iguales.");
	}
	
	@Test
	public void testGetNombre()
	{
		assertEquals("Pepe", pedido.getNombreCliente(), "El nombre del cliente no es el esperado.");
	}

	@Test
	public void precioTotalPedido()
	{
		pedido.agregarProducto(producto1);
		pedido.agregarProducto(producto2);
		ArrayList<ProductoMenu> productos = new ArrayList<>();
		productos.add(producto1);
		productos.add(producto2);
		Combo combo = new Combo("combo especial", 9.5, productos);
		pedido.agregarProducto(producto1);
		pedido.agregarProducto(combo);
		int precioBase = 0;
		for (Producto producto : pedido.getProductos()) 
		{
			precioBase += producto.getPrecio();
		}
		double iva = (int) (precioBase * 0.19);
		int precioTotal = precioBase + (int) iva;
		
		assertEquals(precioBase, pedido.getPrecioNetoPedido(), "El precio neto no es el esperado.");
		assertEquals(iva, pedido.getPrecioIVAPedido(), "El valor del IVA no es el esperdo.");
		assertEquals(precioTotal, pedido.getPrecioTotalPedido(), "El precio total del pedido no es el esperado.");
	}
	
	@Test
	public void testAgregarTextoFactura()
	{
		pedido.agregarProducto(producto1);
		pedido.agregarProducto(producto2);
		ArrayList<ProductoMenu> productos = new ArrayList<>();
		productos.add(producto1);
		productos.add(producto2);
        StringBuffer sb = new StringBuffer( );

        sb.append( "Cliente: " + "Pepe" + "\n" );
        sb.append( "Dirección: " + "Localidad de Kennedy" + "\n" );
        sb.append( "----------------\n" );

        for( Producto item : productos )
        {
            sb.append( item.generarTextoFactura( ) );
        }

        sb.append( "----------------\n" );
        sb.append( "Precio Neto:  " + pedido.getPrecioNetoPedido( ) + "\n" );
        sb.append( "IVA:          " + pedido.getPrecioIVAPedido( ) + "\n" );
        sb.append( "Precio Total: " + pedido.getPrecioTotalPedido( ) + "\n" );
        
        assertEquals(sb.toString(), pedido.generarTextoFactura(), "El texto de factura no es el esperado.");
	}
	
	@Test
	public void testGuardarFactura() throws FileNotFoundException
	{
		pedido.agregarProducto(producto1);
		pedido.agregarProducto(producto2);
		
		File archivoFactura = new File("factura_text_" + pedido.getNombreCliente() + ".txt");
		pedido.guardarFactura(archivoFactura);
		assertTrue(archivoFactura.exists(), "El archivo debe ser creado.");
	}
	}

