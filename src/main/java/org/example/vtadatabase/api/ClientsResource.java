package org.example.vtadatabase.api;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.vtadatabase.api.dto.ClientDto;
import org.example.vtadatabase.infrastructure.persistence.DatabaseServer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * REST API resource for managing clients.
 * Provides endpoints for CRUD operations on client data.
 */
@Path("/clients")
public class ClientsResource {

    private static final String SELECT_ALL_ACTIVE_CLIENTS =
            "SELECT id, name, email FROM demo.client WHERE deleted = false ORDER BY id";
    private static final String SELECT_CLIENT_BY_ID =
            "SELECT id, name, email FROM demo.client WHERE id = ? AND deleted = false";
    private static final String UPDATE_CLIENT =
            "UPDATE demo.client SET name = ?, email = ? WHERE id = ? AND deleted = false";
    private static final String SOFT_DELETE_CLIENT =
            "UPDATE demo.client SET deleted = true WHERE id = ? AND deleted = false";

    private static final String ERROR_LOADING_CLIENTS = "Failed to load clients";
    private static final String ERROR_LOADING_CLIENT = "Failed to load client";
    private static final String ERROR_UPDATING_CLIENT = "Failed to update client";
    private static final String ERROR_DELETING_CLIENT = "Failed to delete client";
    private static final String CLIENT_NOT_FOUND = "Client with id %d not found";

    private final DatabaseServer databaseServer = new DatabaseServer();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ClientDto> getAllClients() {
        try (Connection connection = databaseServer.createDatabase();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_ACTIVE_CLIENTS)) {

            List<ClientDto> clients = new ArrayList<>();
            while (rs.next()) {
                clients.add(mapToClientDto(rs));
            }
            return clients;
        } catch (SQLException e) {
            throw new RuntimeException(ERROR_LOADING_CLIENTS, e);
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ClientDto getClientById(@PathParam("id") Long id) {
        try (Connection connection = databaseServer.createDatabase();
             PreparedStatement stmt = connection.prepareStatement(SELECT_CLIENT_BY_ID)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapToClientDto(rs);
                }
                throw new NotFoundException(String.format(CLIENT_NOT_FOUND, id));
            }
        } catch (SQLException e) {
            throw new RuntimeException(ERROR_LOADING_CLIENT, e);
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ClientDto updateClient(@PathParam("id") Long id, ClientDto clientDto) {
        try (Connection connection = databaseServer.createDatabase();
             PreparedStatement stmt = connection.prepareStatement(UPDATE_CLIENT)) {

            stmt.setString(1, clientDto.name());
            stmt.setString(2, clientDto.email());
            stmt.setLong(3, id);

            int updatedRows = stmt.executeUpdate();
            if (updatedRows == 0) {
                throw new NotFoundException(String.format(CLIENT_NOT_FOUND, id));
            }

            return new ClientDto(id, clientDto.name(), clientDto.email());
        } catch (SQLException e) {
            throw new RuntimeException(ERROR_UPDATING_CLIENT, e);
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteClient(@PathParam("id") Long id) {
        try (Connection connection = databaseServer.createDatabase();
             PreparedStatement stmt = connection.prepareStatement(SOFT_DELETE_CLIENT)) {

            stmt.setLong(1, id);

            int updatedRows = stmt.executeUpdate();
            if (updatedRows == 0) {
                throw new NotFoundException(String.format(CLIENT_NOT_FOUND, id));
            }

            return Response.noContent().build();
        } catch (SQLException e) {
            throw new RuntimeException(ERROR_DELETING_CLIENT, e);
        }
    }

    /**
     * Maps a ResultSet row to a ClientDto object.
     *
     * @param rs the ResultSet positioned at a client row
     * @return a ClientDto with data from the current row
     * @throws SQLException if a database access error occurs
     */
    private ClientDto mapToClientDto(ResultSet rs) throws SQLException {
        return new ClientDto(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("email")
        );
    }
}

