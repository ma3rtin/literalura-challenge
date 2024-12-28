package com.challenge.literalura;

import modelo.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import repositorio.RepositorioAutor;
import repositorio.RepositorioLibro;
import servicio.ConvertirDatosImpl;
import servicio.GutendexApi;

import java.io.IOException;
import java.util.*;

@SpringBootApplication
public class LiteraluraApplication {
	private static final Scanner teclado = new Scanner(System.in);
	private static GutendexApi consumoAPI = new GutendexApi();
	private static ConvertirDatosImpl conversor = new ConvertirDatosImpl();
	private static List<Libro> librosRegistrados = new ArrayList<>();
	private static List<Autor> autoresRegistrados = new ArrayList<>();
	private static RespuestaApiDTO datos;
	private static RepositorioAutor autorRepositorio;
	private static RepositorioLibro libroRepositorio;


	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
		mostrarMenu();
	}

	public static void mostrarMenu() {
		int opcion = 1;
		do {
			var menu = """                   
                    ELIJA UNA OPCION:
                    1- BUSCAR LIBROS POR TITULO
                    2- LISTAR LIBROS
                    3- LISTAR AUTORES
                    4- LISTAR AUTORES VIVOS EN UN DETERMINADO AÑO
                    5- LISTAR LIBROS POR IDIOMA
                    6- TOP 10 LIBROS
                    7- CONSULTAR AUTOR POR NOMBRE
                    0- SALIR
                    """;

			try {
				System.out.println(menu);
				opcion = teclado.nextInt();
				teclado.nextLine();

				switch (opcion) {
					case 1:
						buscarLibroPorTituloWeb();
						break;
					case 2:
						listarLibrosRegistrados();
						break;
					case 3:
						listarAutoresRegistrados();
						break;
					case 4:
						listarAutoresVivosEnUnDeterminadoAño();
						break;
					case 5:
						listarLibrosPorIdioma();
						break;
					case 6:
						Top10LibrosMasDescargadosBaseDatosLocal();
						break;
					case 7:
						ConsultarAutorPorNombre();
						break;

					case 0:
						System.out.println("""             
                               CERRANDO APP....................
                            """);
						break;
					default:
						System.out.println("""
                         ********************  ELEGIR UNA OPCION DE LA LISTA   *********************
                         """);
						break;
				}
			} catch (InputMismatchException e) {
				System.out.println("""
                                **********************  OPCION NO VALIDA  ********************
                                """);
				teclado.nextLine();
			} catch (Throwable e) {
                throw new RuntimeException(e);
            }
        } while (opcion != 0);
	}



	private static RespuestaApiDTO getDatosLibro() throws Throwable {
		var tituloLibro = teclado.nextLine();
		var json = consumoAPI.obtenerLibro(tituloLibro.replace(" ", "+"));
		datos = conversor.obtenerDatos(json, RespuestaApiDTO.class);
		return datos;

	}


	private static Libro crearLibro(LibroDTO datosLibro, Autor autor) {
		Libro libro = new Libro(datosLibro, autor);
		return libroRepositorio.save(libro);
	}


	private static void buscarLibroPorTituloWeb() throws Throwable {
		System.out.println("¿QUE LIBRO DESEAS CONSULTAR?");
		RespuestaApiDTO datos = getDatosLibro();
		if (!datos.resultados().isEmpty()) {
			LibroDTO datosLibro = datos.resultados().get(0);
			AutorDTO datosAutor = datosLibro.autores().get(0);
			Libro libro = null;
			Libro libroDb = libroRepositorio.findByTitulo(datosLibro.titulo());
			if (libroDb != null) {
				System.out.println(libroDb );
			} else {
				Autor autorDb = autorRepositorio.findByNombreIgnoreCase(datosLibro.autores().get(0).nombre());
				if (autorDb == null) {
					Autor autor = new Autor(datosAutor);
					autor = autorRepositorio.save(autor);
					libro = crearLibro(datosLibro, autor);
					System.out.println(libro);
				} else {
					libro = crearLibro(datosLibro, autorDb);
					System.out.println(libro);
				}
			}
		} else {
			System.out.println("""
            EL LIBRO NO EXISTE..............
        """);
		}
	}


	private static void listarLibrosRegistrados() {
		List<Libro> librosRegistrados = libroRepositorio.findAll();
		librosRegistrados.stream()
				.sorted((libro1, libro2) -> libro1.getAutor().getNombre().compareTo(libro2.getAutor().getNombre()))
				.forEach(System.out::println);
	}

	private static void listarAutoresRegistrados() {
		autoresRegistrados = autorRepositorio.findAll();
		autoresRegistrados.stream()
				.sorted((autor1, autor2) -> autor1.getNombre().compareTo(autor2.getNombre()))
				.forEach(System.out::println);
	}


	private static void listarAutoresVivosEnUnDeterminadoAño() {
		System.out.println("INGRESE UN AÑO PARA VALIDAR AUTORES VIVOS: ");
		try {
			int yearQuery = teclado.nextInt();
			teclado.nextLine();
			List<Autor> autoresVivos = autorRepositorio.autorVivosEnDeterminadoYear(yearQuery);
			if(autoresVivos.isEmpty()){
				System.out.println("""
                     ******************* NO HAY AUTORES EN EL AÑO SELECIONADO **********
                """);
			}else{
				autoresVivos.forEach(System.out::println);
			}

		} catch (InputMismatchException e) {
			teclado.nextLine();
			System.out.println("""
                    *********************** INGRESAR EL AÑO EN NUMERO  **********************
                    """);
		}
	}

	private static void listarLibrosPorIdioma() {
		String idioma;
		System.out.println("""
   ************************************************************
                        IDIOMA DISPONIBLE:
                        1 - Español
                        2 - Ingles
                        3 - Frances
                        4 - Portugues
   *************************************************************
""");

		var opcion = teclado.nextInt();
		teclado.nextLine();

		if (opcion == 1) {
			idioma = "es";
		} else if (opcion == 2) {
			idioma = "en";
		} else if (opcion == 3) {
			idioma = "fr";
		} else if (opcion == 4) {
			idioma = "pt";
		}else {
			idioma = null;
			System.out.println("""
            *************************  OPCION NO VALIDA  ************************
            """);
		}

		List<Libro> librosPorIdioma = libroRepositorio.findByIdiomasContaining(idioma);
		if (librosPorIdioma.isEmpty()) {
			System.out.println("""
            ************ NO HAY LIBROS PARA EL IDIOMA SELECIONADO  **************** 
            """);
		} else {
			var  cantidadLibrosPorIdioma =libroRepositorio.countByLanguage(idioma);
			System.out.println( " " + "\n" +

					"**************** HAY " + cantidadLibrosPorIdioma + " LIBROS EN ESTE IDIOMA ******************" + "\n" +

					"  ");
			librosPorIdioma.forEach(System.out::println);
		}

	}


	private static void Top10LibrosMasDescargadosBaseDatosLocal() {
		var top10LibrosDescargados = libroRepositorio.findTop10ByOrderByNumeroDescargasDesc();
		top10LibrosDescargados.forEach(System.out::println);

	}

	private static void ConsultarAutorPorNombre() {
		System.out.println("INGRESE UN AUTOR A CONSULTAR ");
		var nombreAutor = teclado.nextLine();
		var autorPorNombre = autorRepositorio.findByNombreIgnoreCase(nombreAutor);
		if(autorPorNombre != null) {
			var  ID_AUTOR = autorPorNombre.getId();
			var libroEscritosPorEseAutor = libroRepositorio.findByAutor(autorPorNombre);
			System.out.println(
					"************************************************************************************"  + "\n" +
							"**********************************     AUTOR            ****************************"   + "\n" +
							autorPorNombre + "\n" +
							"" + "\n" +
							"***********************  LIBROS ESCRITOS                ****************************"
			);
			libroEscritosPorEseAutor.stream()
					.sorted(Comparator.comparing(Libro::getNumeroDescargas).reversed())
					.forEach(libro -> System.out.println(libro));

		}

	}

}
