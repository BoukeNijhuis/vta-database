package org.example.vtadatabase.api;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.example.vtadatabase.api.dto.ClientDto;
import org.example.vtadatabase.infrastructure.persistence.DatabaseServer;

import java.sql.Connection;
import java.sql.PreparedStatement;
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

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ClientDto updateClient(@PathParam("id") Long id, ClientDto clientDto) {
        try (Connection connection = new DatabaseServer().createDatabase();
             PreparedStatement stmt = connection.prepareStatement(
                     "update demo.client set name = ?, email = ? where id = ?")) {
            stmt.setString(1, clientDto.name());
            stmt.setString(2, clientDto.email());
            stmt.setLong(3, id);
            int updated = stmt.executeUpdate();
            if (updated == 0) {
                throw new NotFoundException("Client with id " + id + " not found");
            }
            return new ClientDto(id, clientDto.name(), clientDto.email());
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update client", e);
        }
    }
}

