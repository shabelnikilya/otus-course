package servlet;

import com.google.gson.Gson;
import crm.service.DBServiceClient;
import dto.ClientDto;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servlet.helpers.DtoUtils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


public class ClientsApiServlet extends HttpServlet {

    private final DBServiceClient dbServiceClient;
    private final Gson gson;

    public ClientsApiServlet(DBServiceClient dbServiceClient, Gson gson) {
        this.dbServiceClient = dbServiceClient;
        this.gson = gson;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<ClientDto> clients = dbServiceClient.findAll().stream()
                .map(DtoUtils::toDto)
                .collect(Collectors.toList());

        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        String o = gson.toJson(clients);
        out.print(gson.toJson(clients));
    }
}
