package servlet;

import com.google.gson.Gson;
import crm.model.Address;
import crm.model.Client;
import crm.model.Phone;
import crm.service.DBServiceClient;
import dto.ClientDto;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import services.TemplateProcessor;
import servlet.helpers.DtoUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class ClientsServlet extends HttpServlet {

    private static final String CLIENTS_PAGE_TEMPLATE = "clients.html";
    private static final String TEMPLATE_CLIENTS = "clients";

    private static final String FORM_CLIENT_NAME = "name";
    private static final String FORM_CLIENT_ADDRESS = "address";
    private static final String FORM_CLIENT_PHONE_NUMBER_1 = "phone-number1";
    private static final String FORM_CLIENT_PHONE_NUMBER_2 = "phone-number2";

    private final DBServiceClient dbServiceClient;
    private final TemplateProcessor templateProcessor;

    public ClientsServlet(TemplateProcessor templateProcessor, DBServiceClient dbServiceClient) {
        this.templateProcessor = templateProcessor;
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();

        List<ClientDto> clients = dbServiceClient.findAll().stream()
                .map(DtoUtils::toDto)
                .collect(Collectors.toList());;

        paramsMap.put(TEMPLATE_CLIENTS, clients);

        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(CLIENTS_PAGE_TEMPLATE, paramsMap));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, String[]> parameters = req.getParameterMap();
        Client client = createClientFromForm(parameters);

        dbServiceClient.saveClient(client);

        resp.sendRedirect("/" + TEMPLATE_CLIENTS);
    }

    private Client createClientFromForm(Map<String, String[]> parameters) {

        List<Phone> phones = new ArrayList<>();
        phones.add(new Phone(null, parameters.get(FORM_CLIENT_PHONE_NUMBER_1)[0]));

        if (parameters.get(FORM_CLIENT_PHONE_NUMBER_2)[0].length() != 0) {
            phones.add(new Phone(null, parameters.get(FORM_CLIENT_PHONE_NUMBER_2)[0]));
        }

        return new Client(
                null,
                parameters.get(FORM_CLIENT_NAME)[0],
                new Address(null, parameters.get(FORM_CLIENT_ADDRESS)[0]),
                phones
        );
    }
}
