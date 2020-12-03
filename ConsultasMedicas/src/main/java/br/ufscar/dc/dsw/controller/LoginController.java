package br.ufscar.dc.dsw.controller;

import br.ufscar.dc.dsw.dao.MedicoDAO;
import br.ufscar.dc.dsw.dao.PacienteDAO;
import br.ufscar.dc.dsw.domain.Medico;
import br.ufscar.dc.dsw.domain.Paciente;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "Index", urlPatterns = { "/login" })
public class LoginController extends HttpServlet {

	private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // renderiza página de login
        RequestDispatcher rd = request.getRequestDispatcher("/login.jsp");
        rd.forward(request, response);
    }    
        
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // verifica o login
        String user = request.getParameter("user");
        String password = request.getParameter("password");
        
        // verifica se é admin
        if("admin".equals(user) && "admin".equals(password)){
            request.getSession().setAttribute("admin", true);
            response.sendRedirect("admin.jsp");
        }
        else{
            // verifica se é médico
            MedicoDAO medicoDAO = new MedicoDAO();
            try {
                ArrayList<Medico> medicos = medicoDAO.getAll();
                for (Medico medico : medicos) {
                    if(medico.getEmail().equals(user) && medico.getSenha().equals(password)){
                        request.getSession().setAttribute("medico", medico);
                        response.sendRedirect("medico.jsp");
                    }
                    else{
                        // verifica se é apciente
                        PacienteDAO pacienteDAO = new PacienteDAO();
                        try {
                            ArrayList<Paciente> pacientes = pacienteDAO.getAll();
                            for (Paciente paciente : pacientes) {
                                if(paciente.getEmail().equals(user) && paciente.getSenha().equals(password)){
                                    request.getSession().setAttribute("paciente", paciente);
                                    response.sendRedirect("paciente.jsp");
                                }
                                else{
                                    response.sendRedirect("login");
                                }
                            }
                        } catch (Exception ex) {
                            request.setAttribute("erro", ex.toString());
                            response.sendRedirect("error.jsp");
                        }
                    }
                }
            } catch (Exception ex) {
                request.setAttribute("erro", ex.toString());
                response.sendRedirect("error.jsp");
            }
        }
    }
}