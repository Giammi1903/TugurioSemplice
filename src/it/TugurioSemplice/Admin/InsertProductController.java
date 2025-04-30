package it.TugurioSemplice.Admin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import DataManagement.ProdottoBean;
import DataManagement.ProdottoDAO;

@WebServlet("/InsertProductController")
@MultipartConfig
public class InsertProductController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	static ProdottoDAO model;

	static {
		model = new ProdottoDAO();
	}

	public InsertProductController() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
	}

	private String extractFileName(Part part) {
		String contentDisp = part.getHeader("content-disposition");
		String[] items = contentDisp.split(";");
		for (String s : items) {
			if (s.trim().startsWith("filename")) {
				return s.substring(s.indexOf("=") + 2, s.length() - 1);
			}
		}
		return "";
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String name = request.getParameter("name");
	    String description = request.getParameter("description");
	    double price = Double.parseDouble(request.getParameter("price"));
	    int quantity = Integer.parseInt(request.getParameter("quantity"));

	    // Ottieni la parte dell'immagine
	    Part imagePart = request.getPart("image");

	    // Ottieni il percorso reale della cartella photo/Bare dentro WebContent
	    String savePath = request.getServletContext().getRealPath("photo/Bare");
	    System.out.println("Percorso assoluto cartella immagini: " + savePath);

	    File fileSaveDir = new File(savePath);
	    if (!fileSaveDir.exists()) {
	        boolean dirCreated = fileSaveDir.mkdirs();
	        System.out.println("Directory creata: " + dirCreated);
	    }

	    // Controlli di sicurezza
	    if (!fileSaveDir.exists() || !fileSaveDir.canWrite()) {
	        System.out.println("ERRORE: Directory non valida o non scrivibile: " + savePath);
	        request.setAttribute("error", "Impossibile creare o scrivere nella directory delle immagini.");
	    } else {
	        // Estrai il nome del file
	        String fileName = extractFileName(imagePart);
	        if (fileName != null && !fileName.isEmpty()) {
	            // Percorso finale dove spostare il file
	            String filePath = savePath + File.separator + fileName;
	            System.out.println("Spostamento immagine in: " + filePath);

	            // Sposta il file dalla cartella temporanea a quella finale
	            String tempFilePath = imagePart.getSubmittedFileName(); // Ottieni il percorso temporaneo

	            try (InputStream inputStream = imagePart.getInputStream();
	                 FileOutputStream outputStream = new FileOutputStream(filePath)) {

	                // Copia il contenuto dal file temporaneo a quello di destinazione
	                byte[] buffer = new byte[1024];
	                int bytesRead;
	                while ((bytesRead = inputStream.read(buffer)) != -1) {
	                    outputStream.write(buffer, 0, bytesRead);
	                }

	                // Verifica se il file è stato spostato correttamente
	                File savedFile = new File(filePath);
	                if (savedFile.exists()) {
	                    System.out.println("SUCCESSO: File spostato correttamente.");

	                    // Crea il bean del prodotto
	                    ProdottoBean bean = new ProdottoBean();
	                    bean.setNome(name);
	                    bean.setDescrizione(description);
	                    bean.setPrezzoBase(price);
	                    bean.setQuantita(quantity);
	                    bean.setDisponibilita(quantity > 0);
	                    bean.setIva(22);

	                    // Percorso relativo per il DB
	                    String dbImagePath = "photo/Bare/" + fileName;
	                    bean.setImmagine(dbImagePath);

	                    try {
	                        model.doSave(bean);
	                        request.setAttribute("message", "Prodotto aggiunto con successo.");
	                    } catch (SQLException e) {
	                        e.printStackTrace();
	                        request.setAttribute("error", "Errore nel salvataggio sul database: " + e.getMessage());
	                    }
	                } else {
	                    System.out.println("ERRORE: Impossibile spostare il file.");
	                    request.setAttribute("error", "Errore nel salvataggio dell'immagine.");
	                }
	            } catch (IOException e) {
	                e.printStackTrace();
	                System.out.println("ERRORE: Eccezione durante lo spostamento del file.");
	                request.setAttribute("error", "Errore durante lo spostamento del file.");
	            }
	        } else {
	            System.out.println("ERRORE: Nome file non valido.");
	            request.setAttribute("error", "Nome file immagine non valido.");
	        }
	    }

	    // Forward alla pagina JSP
	    getServletContext().getRequestDispatcher("/ProductView.jsp").forward(request, response);
	}


}
