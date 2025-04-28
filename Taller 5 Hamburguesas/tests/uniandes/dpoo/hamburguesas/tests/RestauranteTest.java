package uniandes.dpoo.hamburguesas.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.excepciones.HamburguesaException;
import uniandes.dpoo.hamburguesas.excepciones.IngredienteRepetidoException;
import uniandes.dpoo.hamburguesas.excepciones.NoHayPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.excepciones.ProductoFaltanteException;
import uniandes.dpoo.hamburguesas.excepciones.ProductoRepetidoException;
import uniandes.dpoo.hamburguesas.excepciones.YaHayUnPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.mundo.Combo;
import uniandes.dpoo.hamburguesas.mundo.Ingrediente;
import uniandes.dpoo.hamburguesas.mundo.Pedido;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;
import uniandes.dpoo.hamburguesas.mundo.Restaurante;

public class RestauranteTest {
	
	private Restaurante restaurante;
	
	@BeforeEach
    void setUp( ) throws Exception
    {
		restaurante = new Restaurante();
		
    }
	
	@AfterEach
    void tearDown( ) throws Exception
    {
    }
	
	@Test
    void testIniciarPedido( ) throws YaHayUnPedidoEnCursoException
    {
		Pedido.numeroPedidos = 0;
		restaurante.iniciarPedido("Pepe", "Dg. 1 # 2 - 3");
		assertEquals( 1, restaurante.getPedidoEnCurso().getIdPedido(), "El id del pedido no es el esperado." );
		assertEquals( "Pepe", restaurante.getPedidoEnCurso().getNombreCliente(), "El nombre del cliente no es el esperado." );
		assertThrows(YaHayUnPedidoEnCursoException.class, () -> {
	        restaurante.iniciarPedido("Ernesto", "Localidad de Kennedy");
	    });
    }
	@Test
    void testCerrarYGuardarPedido( ) throws NoHayPedidoEnCursoException,  IOException, YaHayUnPedidoEnCursoException
    {
		Pedido.numeroPedidos = 0;
		assertThrows(NoHayPedidoEnCursoException.class, () -> {
	        restaurante.cerrarYGuardarPedido();
	    });
		restaurante.iniciarPedido("Pepe", "Dg. 1 # 2 - 3");
		ProductoMenu producto1 = new ProductoMenu("corral", 14000);
		restaurante.getPedidoEnCurso().agregarProducto(producto1);
		Restaurante.CARPETA_FACTURAS = "Z:/una/ruta/que/no/existe/";
		assertThrows(IOException.class, () -> {
	        restaurante.cerrarYGuardarPedido();
	    });
		Restaurante.CARPETA_FACTURAS = "./facturas/";
		restaurante.cerrarYGuardarPedido();
		String nombreArchivoEsperado = "factura_" + 1 + ".txt";
		File archivoEsperado = new File(Restaurante.CARPETA_FACTURAS + nombreArchivoEsperado);
		assertEquals( 1, restaurante.getPedidos().size(), "El pedido no se agrego al historico");
		assertTrue(archivoEsperado.exists(), "El archivo de factura no fue creado correctamente.");
		assertTrue(archivoEsperado.length() > 0, "El archivo de factura está vacío.");
		
		restaurante.iniciarPedido("Ernesto", "Localidad de Kennedy");
		ProductoMenu producto2 = new ProductoMenu("corral queso", 16000);
		restaurante.getPedidoEnCurso().agregarProducto(producto2);
		restaurante.cerrarYGuardarPedido();
		String nombreArchivoEsperado2 = "factura_" + 1 + ".txt";
		File archivoEsperado2 = new File(Restaurante.CARPETA_FACTURAS + nombreArchivoEsperado2);
		assertEquals( 2, restaurante.getPedidos().size(), "El pedido no se agrego al historico");
		assertTrue(archivoEsperado2.exists(), "El archivo de factura no fue creado correctamente.");
		assertTrue(archivoEsperado2.length() > 0, "El archivo de factura está vacío.");
    }
	
	@Test
    void testGetPedidoEnCurso( ) throws YaHayUnPedidoEnCursoException
    {
		Pedido.numeroPedidos = 0;
		assertEquals( null, restaurante.getPedidoEnCurso(), "No deberian haber pedidos en curso.");
		restaurante.iniciarPedido("Pepe", "Dg. 1 # 2 - 3");
        assertEquals("Pepe", restaurante.getPedidoEnCurso().getNombreCliente(), "El nombre del cliente no es el esperado.");
        assertEquals( 1, restaurante.getPedidoEnCurso().getIdPedido(), "El id del pedido no es el esperado.");
    }
	
	@Test
    void testGetPedidos( ) throws YaHayUnPedidoEnCursoException, NoHayPedidoEnCursoException, IOException
    {
		Pedido.numeroPedidos = 0;
		assertEquals( 0, restaurante.getPedidos().size(), "No deberian haber pedidos en el historico");
		restaurante.iniciarPedido("Pepe", "Dg. 1 # 2 - 3");
		restaurante.getPedidos().add(restaurante.getPedidoEnCurso());
        assertEquals(1, restaurante.getPedidos().size(), "Deberia haber un pedido en el historico");
        assertEquals("Pepe", restaurante.getPedidos().get(0).getNombreCliente(), "El nombre del cliente no es el esperado.");
        assertEquals( 1, restaurante.getPedidos().get(0).getIdPedido(), "El id del pedido no es el esperado.");
    }
	
	@Test
    void testGetMenuBase( )
    {
		assertEquals( 0, restaurante.getMenuBase().size(), "No deberian haber productos en el menu");
		ProductoMenu producto = new ProductoMenu("corral", 14000);
		restaurante.getMenuBase().add(producto);
		assertEquals( 1, restaurante.getMenuBase().size(), "Deberia haber un elemento");
		assertEquals("corral", restaurante.getMenuBase().get(0).getNombre(), "El nombre del producto no es el esperado.");
        assertEquals( 14000, restaurante.getMenuBase().get(0).getPrecio(), "El precio del producto no es el esperado.");
    }
	
	@Test
    void testGetMenuCombos( )
    {
		assertEquals( 0, restaurante.getMenuCombos().size(), "No deberian haber productos en el menu");
		ProductoMenu producto1 = new ProductoMenu("corral", 14000);
		ProductoMenu producto2 = new ProductoMenu("papas medianas", 5500);
		ProductoMenu producto3 = new ProductoMenu("gaseosa", 5000);
		ArrayList<ProductoMenu> items = new ArrayList<>();
		items.add(producto1);
		items.add(producto2);
		items.add(producto3);
		Combo combo = new Combo("combo corral", 0.10, items);
		restaurante.getMenuCombos().add(combo);
		assertEquals( 1, restaurante.getMenuCombos().size(), "Deberia haber un elemento");
		assertEquals("combo corral", restaurante.getMenuCombos().get(0).getNombre(), "El nombre del combo no es el esperado.");
        assertEquals( 22050, restaurante.getMenuCombos().get(0).getPrecio(), "El precio del combo no es el esperado.");
    }
	
	@Test
	void testGetIngredientes()
	{
		assertEquals( 0, restaurante.getIngredientes().size(), "No deberian haber ingredientes");
		Ingrediente ingrediente = new Ingrediente("lechuga", 1000);
		restaurante.getIngredientes().add(ingrediente);
		assertEquals( 1, restaurante.getIngredientes().size(), "Deberia haber un elemento");
		assertEquals("lechuga", restaurante.getIngredientes().get(0).getNombre(), "El nombre del ingrediente no es el esperado.");
        assertEquals( 1000, restaurante.getIngredientes().get(0).getCostoAdicional(), "El precio del ingrediente no es el esperado.");
	}
	
	@Test
	public void testCargarInformacionRestaurante() throws IOException, HamburguesaException {

        File archivoIngredientes = new File("data/ingredientes.txt");
        File archivoMenu = new File("data/menu.txt");
        File archivoCombos = new File("data/combos.txt");
        
        restaurante.cargarInformacionRestaurante(archivoIngredientes, archivoMenu, archivoCombos);
        
        ArrayList<Ingrediente> ingredientes = restaurante.getIngredientes();
        assertNotNull("La lista de ingredientes no debería ser null", ingredientes);
        assertFalse("La lista de ingredientes no debería estar vacía", ingredientes.isEmpty());
        
        // Check that menu items were loaded
        ArrayList<ProductoMenu> menuBase = restaurante.getMenuBase();
        assertNotNull("La lista de productos del menú no debería ser null", menuBase);
        assertFalse("La lista de productos del menú no debería estar vacía", menuBase.isEmpty());

        ArrayList<Combo> menuCombos = restaurante.getMenuCombos();
        assertNotNull("La lista de combos no debería ser null", menuCombos);
        assertFalse("La lista de combos no debería estar vacía", menuCombos.isEmpty());
    }
    
    @Test
    public void testCargarIngredientesConIngredienteRepetido() throws IOException {

        File tempFile = File.createTempFile("ingredientes_duplicados", ".txt");
        java.io.FileWriter writer = new java.io.FileWriter(tempFile);
        writer.write("lechuga;1000\n");
        writer.write("tomate;2000\n");
        writer.write("lechuga;1500\n");
        writer.close();

        assertThrows(IngredienteRepetidoException.class, () -> {
            restaurante.cargarInformacionRestaurante(tempFile, 
                                                   new File("data/menu.txt"), 
                                                   new File("data/combos.txt"));
        });
    }
    
    @Test
    public void testCargarMenuConProductoRepetido() throws IOException {

        File tempFile = File.createTempFile("menu_duplicado", ".txt");
        java.io.FileWriter writer = new java.io.FileWriter(tempFile);
        writer.write("hamburguesa;10000\n");
        writer.write("papas;5000\n");
        writer.write("hamburguesa;12000\n");
        writer.close();

        assertThrows(ProductoRepetidoException.class, () -> {
            restaurante.cargarInformacionRestaurante(new File("data/ingredientes.txt"), 
                                                   tempFile, 
                                                   new File("data/combos.txt"));
        });
    }
    
    @Test
    public void testCargarCombosConComboRepetido() throws IOException {

        File tempFile = File.createTempFile("combos_duplicados", ".txt");
        java.io.FileWriter writer = new java.io.FileWriter(tempFile);
        writer.write("combo corral;10%;corral;papas medianas;gaseosa\n");
        writer.write("combo especial;15%;especial;papas grandes;gaseosa\n");
        writer.write("combo corral;12%;corral;papas pequeñas;gaseosa\n"); 
        writer.close();
        
        assertThrows(ProductoRepetidoException.class, () -> {
            restaurante.cargarInformacionRestaurante(new File("data/ingredientes.txt"), 
                                                   new File("data/menu.txt"), 
                                                   tempFile);
        });
    }
    
    @Test
    public void testCargarCombosConProductoFaltante() throws IOException, ProductoRepetidoException {

        File archivoMenu = new File("data/menu.txt");
        restaurante.cargarMenu(archivoMenu);
        
        File tempFile = File.createTempFile("combos_faltantes", ".txt");
        java.io.FileWriter writer = new java.io.FileWriter(tempFile);
        writer.write("combo inexistente;10%;producto_que_no_existe;papas medianas;gaseosa\n");
        writer.close();

        assertThrows(ProductoFaltanteException.class, () -> {
            restaurante.cargarCombos(tempFile);
        });
    }
    
    @Test
    public void testVerificacionDatosIngredientes() throws IOException, HamburguesaException {

        File archivoIngredientes = new File("data/ingredientes.txt");

        restaurante.cargarIngredientes(archivoIngredientes);
        ArrayList<Ingrediente> ingredientes = restaurante.getIngredientes();

        boolean foundTomate = false;
        boolean foundCebolla = false;
        
        for (Ingrediente ing : ingredientes) {
            if (ing.getNombre().equals("tomate")) {
                foundTomate = true;

            }
            if (ing.getNombre().equals("cebolla")) {
                foundCebolla = true;
            }
        }
        
        assertTrue("No se encontró el ingrediente 'lechuga'", foundTomate);
        assertTrue("No se encontró el ingrediente 'tomate'", foundCebolla);
    }
    
    @Test
    public void testVerificacionDatosMenu() throws IOException, HamburguesaException {

        File archivoMenu = new File("data/menu.txt");

        restaurante.cargarMenu(archivoMenu);
        ArrayList<ProductoMenu> menuBase = restaurante.getMenuBase();

        boolean foundHamburguesa = false;
        boolean foundPapas = false;
        
        for (ProductoMenu prod : menuBase) {
            if (prod.getNombre().equals("corral")) {
                foundHamburguesa = true;

            }
            if (prod.getNombre().equals("papas medianas")) {
                foundPapas = true;
            }
        }
        
        assertTrue("No se encontró el producto 'corral'", foundHamburguesa);
        assertTrue("No se encontró el producto 'papas medianas'", foundPapas);
    }
    
    @Test
    public void testVerificacionDatosCombos() throws IOException, HamburguesaException {
        File archivoMenu = new File("data/menu.txt");
        restaurante.cargarMenu(archivoMenu);

        File archivoCombos = new File("data/combos.txt");

        restaurante.cargarCombos(archivoCombos);
        ArrayList<Combo> menuCombos = restaurante.getMenuCombos();

        boolean foundComboCorral = false;
        
        for (Combo combo : menuCombos) {
            if (combo.getNombre().equals("combo corral")) {
                foundComboCorral = true;

            }
        }
        
        assertTrue("No se encontró el combo 'combo corral'", foundComboCorral);
    }
    
    @Test
    public void testCalculoPrecioCombos() throws IOException, HamburguesaException {

        File archivoIngredientes = new File("data/ingredientes.txt");
        File archivoMenu = new File("data/menu.txt");
        File archivoCombos = new File("data/combos.txt");

        restaurante.cargarInformacionRestaurante(archivoIngredientes, archivoMenu, archivoCombos);
        ArrayList<Combo> menuCombos = restaurante.getMenuCombos();

        for (Combo combo : menuCombos) {
            int precioCalculado = combo.getPrecio();
            int precioEsperado = calcularPrecioEsperadoCombo(combo);
            assertEquals(precioEsperado, precioCalculado, "El precio del combo " + combo.getNombre() + " no es correcto");
        }
    }
    
    private int calcularPrecioEsperadoCombo(Combo combo) {
        ArrayList<ProductoMenu> itemsCombo = combo.getItemsCombo();
        int precioTotal = 0;
        
        for (ProductoMenu item : itemsCombo) {
            precioTotal += item.getPrecio();
        }

        return (int) Math.round(precioTotal * (1 - combo.getDescuento()));
    }
}