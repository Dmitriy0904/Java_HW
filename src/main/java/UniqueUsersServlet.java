import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class UniqueUsersServlet extends HttpServlet {
    private final Map<String, String> usersInfo = new ConcurrentHashMap<>();

    @Override
    public void init() {
        log("The servlet: " + getServletName() + " has been successfully initialized");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        String reqIpAdress = req.getRemoteAddr();
        String reqHeaderData = req.getHeader("User-Agent");

        log("Get request from: " + reqIpAdress);
        usersInfo.put(reqIpAdress, reqHeaderData);
        PrintWriter writer = resp.getWriter();

        writer.write("""
                <table border="1" width="100%">
                <tr align="center">
                    <th>IP-Adress</th>
                    <th>User-Agent data</th>
                </tr>""");

        for (Map.Entry<String, String> user : usersInfo.entrySet()) {
            String userIp = user.getKey();
            String userHeaderData = user.getValue();
            String tableRecord;

            tableRecord = String.format("""
                                <tr align="center"> 
                                    <td>
                                        <b>%s</b>
                                    </td>
                                    <td>
                                        <b>%s</b>
                                    </td>
                                </tr>""" , userIp, userHeaderData);

            writer.write(tableRecord);
        }
        writer.write("</table>");
    }


    @Override
    public void destroy() {
        log("The servlet: " + getServletName() + " has been successfully destroyed");
    }
}