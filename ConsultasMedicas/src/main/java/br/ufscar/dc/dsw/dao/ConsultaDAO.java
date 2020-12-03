package br.ufscar.dc.dsw.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import br.ufscar.dc.dsw.domain.Consulta;

public class ConsultaDAO extends BaseDAO {

    public void insert(Consulta consulta) throws Exception {    
        // verifica se já não existe uma consulta nesse horario para esse medico
        // ou esse paciente
        String sql = "select * from Consulta where ano=? and mes=? and dia=? and "
        + "hora=? and minuto=? and cpf_paciente = ?";
        try {
            Connection conn = this.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, consulta.getAno());
            statement.setInt(2, consulta.getMes());
            statement.setInt(3, consulta.getDia());
            statement.setInt(4, consulta.getHora());
            statement.setInt(5, consulta.getMinuto());
            statement.setString(6, consulta.getCpf_paciente());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                throw new Exception("Esse paciente já tem uma consulta nesse horário");
            }
            resultSet.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        
        sql = "select * from Consulta where ano=? and mes=? and dia=? and hora=?"
        + " and minuto=? and crm_medico=?";
        try {
            Connection conn = this.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, consulta.getAno());
            statement.setInt(2, consulta.getMes());
            statement.setInt(3, consulta.getDia());
            statement.setInt(4, consulta.getHora());
            statement.setInt(5, consulta.getMinuto());
            statement.setString(6, consulta.getCrm_medico());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                throw new Exception("Esse medico já tem uma consulta nesse horário");
            }
            resultSet.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        
        
        sql = "insert into Consulta(cpf_paciente, crm_medico, dia, mes,"
        + " ano, hora, minuto) values(?, ?, ?, ?, ?, ?, ?)";
        
        try {
            Connection conn = this.getConnection();
            PreparedStatement statement;
            statement = conn.prepareStatement(sql);
            statement.setString(1, consulta.getCpf_paciente());
            statement.setString(2, consulta.getCrm_medico());
            statement.setInt(3, consulta.getDia());
            statement.setInt(4, consulta.getMes());
            statement.setInt(5, consulta.getAno());
            statement.setInt(6, consulta.getHora());
            statement.setInt(7, consulta.getMinuto());
            statement.executeUpdate();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    public List<Consulta> getAll() throws Exception {   
        List<Consulta> listaConsultas = new ArrayList<>();
        String sql = "SELECT * from Consulta c";
        try {
            Connection conn = this.getConnection();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Consulta consulta = convertConsultaRowIntoConsultaObj(resultSet);
                listaConsultas.add(consulta);
            }
            resultSet.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listaConsultas;
    }
    
    public void delete(Long id) {
        String sql = "DELETE FROM Consulta where id = ?";
        try {
            Connection conn = this.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setLong(1, id);
            statement.executeUpdate();
            statement.close();
            conn.close();
        } catch (SQLException e) {
        }
    }
    
    public Consulta getbyId(Long id) throws Exception {
        Consulta consulta = null;
        String sql = "SELECT * from Consulta WHERE id = ?";
        try {
            Connection conn = this.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                consulta = convertConsultaRowIntoConsultaObj(resultSet);
            }
            resultSet.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return consulta;
    }
    
    public ArrayList<Consulta> getbyPatient(String cpf) throws Exception {
        String sql = "SELECT * from Consulta WHERE cpf_paciente = ?";
        ArrayList<Consulta> listaConsultas = new ArrayList<>();
        try {
            Connection conn = this.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, cpf);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Consulta consulta = convertConsultaRowIntoConsultaObj(resultSet);
                listaConsultas.add(consulta);
            }
            resultSet.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listaConsultas;
    }
    
    public ArrayList<Consulta> getbymedico(String crm) throws Exception {
        String sql = "SELECT * from Consulta WHERE crm_medico = ?";
        ArrayList<Consulta> listaConsultas = new ArrayList<>();
        try {
            Connection conn = this.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, crm);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Consulta consulta = convertConsultaRowIntoConsultaObj(resultSet);
                listaConsultas.add(consulta);
            }
            resultSet.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listaConsultas;
    }

    private Consulta convertConsultaRowIntoConsultaObj(ResultSet resultSet) 
    throws SQLException, Exception {
        Consulta consulta = new Consulta();
        consulta.setCpf_paciente(resultSet.getString("cpf_paciente"));
        consulta.setCrm_medico(resultSet.getString("crm_medico"));
        consulta.setId(resultSet.getLong("id"));
        int ano = resultSet.getInt("ano");
        int mes = resultSet.getInt("mes");
        int dia = resultSet.getInt("dia");
        int hora = resultSet.getInt("hora");
        int minuto = resultSet.getInt("minuto");
        consulta.setData(ano, mes, dia, hora, minuto);
        return consulta;
    }
}