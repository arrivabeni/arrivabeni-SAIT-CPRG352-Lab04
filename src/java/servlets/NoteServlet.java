package servlets;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Note;

public class NoteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String jspDest = "/WEB-INF/viewnote.jsp";
        
        // Determine if edit mode or not
        String editParam = request.getParameter("edit");
        boolean isEditMode = false;
        if (editParam != null) {
            isEditMode = true;
            jspDest = "/WEB-INF/editnote.jsp";
        }

        // get the Note object with file's content and set as 
        // an attribute in the request object
        request.setAttribute("note", getNoteFromFile(isEditMode));

        // Load a JSP from this servlet
        getServletContext().getRequestDispatcher(jspDest).forward(request, response);
        // After a JSP has been loaded, stop the code call
        return;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Exit from editMode
        request.setAttribute("isEditMode", false);

        // Capture the incoming parameters from the JSP
        String title = request.getParameter("title");
        String content = request.getParameter("content");

        Note note = new Note(title, content);
        saveNoteToFile(note);

        // Reads the file again to send to the JSP. The reason to read from file
        // again is that the line breaks handling were implemented in this routine,
        // once the file is small I don't see a reason to not do that
        request.setAttribute("note", getNoteFromFile(false));

        // Load a JSP from this servlet
        getServletContext().getRequestDispatcher("/WEB-INF/viewnote.jsp").forward(request, response);
        // After a JSP has been loaded, stop the code call
        return;
    }

    protected Note getNoteFromFile(boolean isEditMode) {
        String path = getServletContext().getRealPath("/WEB-INF/note.txt");
        Note note = new Note();
        try {
            // Read the file
            BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
            String line;
            String content = "";
            boolean isTitle = true;
            String lineSeparator = "";

            if (isEditMode) {
                lineSeparator = "\n";
            } else {
                lineSeparator = "<br>";
            }

            while ((line = reader.readLine()) != null) {
                if (isTitle) {
                    note.setTitle(line);
                    isTitle = false;
                } else {
                    if (content.isEmpty()) {
                        content += line;
                    } else {
                        content += lineSeparator + line;
                    }
                }
            }
            note.setContent(content);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(NoteServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(NoteServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Return even if empty object to prevent exception to the user
        return note;
    }

    protected void saveNoteToFile(Note note) {
        String path = getServletContext().getRealPath("/WEB-INF/note.txt");

        try {
            try (PrintWriter output = new PrintWriter(new BufferedWriter(new FileWriter(path, false)))) {
                output.print(note.getTitle() + "\n");
                output.print(note.getContent());
            }

        } catch (IOException ex) {
            Logger.getLogger(NoteServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
