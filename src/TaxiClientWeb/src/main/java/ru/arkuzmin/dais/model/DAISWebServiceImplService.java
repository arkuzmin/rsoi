
package ru.arkuzmin.dais.model;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebServiceClient(name = "DAISWebServiceImplService", targetNamespace = "http://model.dais.arkuzmin.ru/", wsdlLocation = "http://localhost:8080/DispAISWeb/dais?wsdl")
public class DAISWebServiceImplService
    extends Service
{

    private final static URL DAISWEBSERVICEIMPLSERVICE_WSDL_LOCATION;
    private final static Logger logger = Logger.getLogger(ru.arkuzmin.dais.model.DAISWebServiceImplService.class.getName());

    static {
        URL url = null;
        try {
            URL baseUrl;
            baseUrl = ru.arkuzmin.dais.model.DAISWebServiceImplService.class.getResource(".");
            url = new URL(baseUrl, "http://localhost:8080/DispAISWeb/dais?wsdl");
        } catch (MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: 'http://localhost:8080/DispAISWeb/dais?wsdl', retrying as a local file");
            logger.warning(e.getMessage());
        }
        DAISWEBSERVICEIMPLSERVICE_WSDL_LOCATION = url;
    }

    public DAISWebServiceImplService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public DAISWebServiceImplService() {
        super(DAISWEBSERVICEIMPLSERVICE_WSDL_LOCATION, new QName("http://model.dais.arkuzmin.ru/", "DAISWebServiceImplService"));
    }

    /**
     * 
     * @return
     *     returns DAISWebService
     */
    @WebEndpoint(name = "DAISWebServiceImplPort")
    public DAISWebService getDAISWebServiceImplPort() {
        return super.getPort(new QName("http://model.dais.arkuzmin.ru/", "DAISWebServiceImplPort"), DAISWebService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns DAISWebService
     */
    @WebEndpoint(name = "DAISWebServiceImplPort")
    public DAISWebService getDAISWebServiceImplPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://model.dais.arkuzmin.ru/", "DAISWebServiceImplPort"), DAISWebService.class, features);
    }

}
