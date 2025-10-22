package org.example.vtadatabase.api;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.example.vtadatabase.api.dto.ClientDto;
import org.example.vtadatabase.infrastructure.persistence.DatabaseServer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Path("/clients")
public class ClientsResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ClientDto> getAllClients() {
        try (Connection connection = new DatabaseServer().createDatabase();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("select id, name, email from demo.client order by id")) {
            List<ClientDto> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new ClientDto(rs.getLong("id"), rs.getString("name"), rs.getString("email")));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load clients", e);
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ClientDto getClientById(@PathParam("id") Long id) {
        try (Connection connection = new DatabaseServer().createDatabase();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("select id, name, email from demo.client where id = " + id)) {
            if (rs.next()) {
                return new ClientDto(rs.getLong("id"), rs.getString("name"), rs.getString("email"));
            }
            throw new NotFoundException("Client with id " + id + " not found");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load client", e);
        }
    }
}

