
package ru.arkuzmin.dais.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ru.arkuzmin.dais.model package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _AuthorizeUser_QNAME = new QName("http://model.dais.arkuzmin.ru/", "authorizeUser");
    private final static QName _ConfirmApplicationResponse_QNAME = new QName("http://model.dais.arkuzmin.ru/", "confirmApplicationResponse");
    private final static QName _GetUserHistory_QNAME = new QName("http://model.dais.arkuzmin.ru/", "getUserHistory");
    private final static QName _AddGuestApplicationResponse_QNAME = new QName("http://model.dais.arkuzmin.ru/", "addGuestApplicationResponse");
    private final static QName _AddGuestApplication_QNAME = new QName("http://model.dais.arkuzmin.ru/", "addGuestApplication");
    private final static QName _GetStatus_QNAME = new QName("http://model.dais.arkuzmin.ru/", "getStatus");
    private final static QName _AddUserApplication_QNAME = new QName("http://model.dais.arkuzmin.ru/", "addUserApplication");
    private final static QName _GetStatusResponse_QNAME = new QName("http://model.dais.arkuzmin.ru/", "getStatusResponse");
    private final static QName _AddUserApplicationResponse_QNAME = new QName("http://model.dais.arkuzmin.ru/", "addUserApplicationResponse");
    private final static QName _AuthorizeUserResponse_QNAME = new QName("http://model.dais.arkuzmin.ru/", "authorizeUserResponse");
    private final static QName _RegisterNewUser_QNAME = new QName("http://model.dais.arkuzmin.ru/", "registerNewUser");
    private final static QName _RegisterNewUserResponse_QNAME = new QName("http://model.dais.arkuzmin.ru/", "registerNewUserResponse");
    private final static QName _GetUserHistoryResponse_QNAME = new QName("http://model.dais.arkuzmin.ru/", "getUserHistoryResponse");
    private final static QName _ConfirmApplication_QNAME = new QName("http://model.dais.arkuzmin.ru/", "confirmApplication");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ru.arkuzmin.dais.model
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ConfirmApplication }
     * 
     */
    public ConfirmApplication createConfirmApplication() {
        return new ConfirmApplication();
    }

    /**
     * Create an instance of {@link RegisterNewUserResponse }
     * 
     */
    public RegisterNewUserResponse createRegisterNewUserResponse() {
        return new RegisterNewUserResponse();
    }

    /**
     * Create an instance of {@link Order }
     * 
     */
    public Order createOrder() {
        return new Order();
    }

    /**
     * Create an instance of {@link Status }
     * 
     */
    public Status createStatus() {
        return new Status();
    }

    /**
     * Create an instance of {@link RegisterNewUser }
     * 
     */
    public RegisterNewUser createRegisterNewUser() {
        return new RegisterNewUser();
    }

    /**
     * Create an instance of {@link ConfirmApplicationResponse }
     * 
     */
    public ConfirmApplicationResponse createConfirmApplicationResponse() {
        return new ConfirmApplicationResponse();
    }

    /**
     * Create an instance of {@link AddGuestApplication }
     * 
     */
    public AddGuestApplication createAddGuestApplication() {
        return new AddGuestApplication();
    }

    /**
     * Create an instance of {@link AddUserApplication }
     * 
     */
    public AddUserApplication createAddUserApplication() {
        return new AddUserApplication();
    }

    /**
     * Create an instance of {@link AuthorizeUserResponse }
     * 
     */
    public AuthorizeUserResponse createAuthorizeUserResponse() {
        return new AuthorizeUserResponse();
    }

    /**
     * Create an instance of {@link AuthorizeUser }
     * 
     */
    public AuthorizeUser createAuthorizeUser() {
        return new AuthorizeUser();
    }

    /**
     * Create an instance of {@link GetUserHistoryResponse }
     * 
     */
    public GetUserHistoryResponse createGetUserHistoryResponse() {
        return new GetUserHistoryResponse();
    }

    /**
     * Create an instance of {@link GetStatusResponse }
     * 
     */
    public GetStatusResponse createGetStatusResponse() {
        return new GetStatusResponse();
    }

    /**
     * Create an instance of {@link GetUserHistory }
     * 
     */
    public GetUserHistory createGetUserHistory() {
        return new GetUserHistory();
    }

    /**
     * Create an instance of {@link GetStatus }
     * 
     */
    public GetStatus createGetStatus() {
        return new GetStatus();
    }

    /**
     * Create an instance of {@link AddUserApplicationResponse }
     * 
     */
    public AddUserApplicationResponse createAddUserApplicationResponse() {
        return new AddUserApplicationResponse();
    }

    /**
     * Create an instance of {@link User }
     * 
     */
    public User createUser() {
        return new User();
    }

    /**
     * Create an instance of {@link AddGuestApplicationResponse }
     * 
     */
    public AddGuestApplicationResponse createAddGuestApplicationResponse() {
        return new AddGuestApplicationResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AuthorizeUser }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://model.dais.arkuzmin.ru/", name = "authorizeUser")
    public JAXBElement<AuthorizeUser> createAuthorizeUser(AuthorizeUser value) {
        return new JAXBElement<AuthorizeUser>(_AuthorizeUser_QNAME, AuthorizeUser.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConfirmApplicationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://model.dais.arkuzmin.ru/", name = "confirmApplicationResponse")
    public JAXBElement<ConfirmApplicationResponse> createConfirmApplicationResponse(ConfirmApplicationResponse value) {
        return new JAXBElement<ConfirmApplicationResponse>(_ConfirmApplicationResponse_QNAME, ConfirmApplicationResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUserHistory }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://model.dais.arkuzmin.ru/", name = "getUserHistory")
    public JAXBElement<GetUserHistory> createGetUserHistory(GetUserHistory value) {
        return new JAXBElement<GetUserHistory>(_GetUserHistory_QNAME, GetUserHistory.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddGuestApplicationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://model.dais.arkuzmin.ru/", name = "addGuestApplicationResponse")
    public JAXBElement<AddGuestApplicationResponse> createAddGuestApplicationResponse(AddGuestApplicationResponse value) {
        return new JAXBElement<AddGuestApplicationResponse>(_AddGuestApplicationResponse_QNAME, AddGuestApplicationResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddGuestApplication }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://model.dais.arkuzmin.ru/", name = "addGuestApplication")
    public JAXBElement<AddGuestApplication> createAddGuestApplication(AddGuestApplication value) {
        return new JAXBElement<AddGuestApplication>(_AddGuestApplication_QNAME, AddGuestApplication.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetStatus }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://model.dais.arkuzmin.ru/", name = "getStatus")
    public JAXBElement<GetStatus> createGetStatus(GetStatus value) {
        return new JAXBElement<GetStatus>(_GetStatus_QNAME, GetStatus.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddUserApplication }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://model.dais.arkuzmin.ru/", name = "addUserApplication")
    public JAXBElement<AddUserApplication> createAddUserApplication(AddUserApplication value) {
        return new JAXBElement<AddUserApplication>(_AddUserApplication_QNAME, AddUserApplication.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetStatusResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://model.dais.arkuzmin.ru/", name = "getStatusResponse")
    public JAXBElement<GetStatusResponse> createGetStatusResponse(GetStatusResponse value) {
        return new JAXBElement<GetStatusResponse>(_GetStatusResponse_QNAME, GetStatusResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddUserApplicationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://model.dais.arkuzmin.ru/", name = "addUserApplicationResponse")
    public JAXBElement<AddUserApplicationResponse> createAddUserApplicationResponse(AddUserApplicationResponse value) {
        return new JAXBElement<AddUserApplicationResponse>(_AddUserApplicationResponse_QNAME, AddUserApplicationResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AuthorizeUserResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://model.dais.arkuzmin.ru/", name = "authorizeUserResponse")
    public JAXBElement<AuthorizeUserResponse> createAuthorizeUserResponse(AuthorizeUserResponse value) {
        return new JAXBElement<AuthorizeUserResponse>(_AuthorizeUserResponse_QNAME, AuthorizeUserResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RegisterNewUser }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://model.dais.arkuzmin.ru/", name = "registerNewUser")
    public JAXBElement<RegisterNewUser> createRegisterNewUser(RegisterNewUser value) {
        return new JAXBElement<RegisterNewUser>(_RegisterNewUser_QNAME, RegisterNewUser.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RegisterNewUserResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://model.dais.arkuzmin.ru/", name = "registerNewUserResponse")
    public JAXBElement<RegisterNewUserResponse> createRegisterNewUserResponse(RegisterNewUserResponse value) {
        return new JAXBElement<RegisterNewUserResponse>(_RegisterNewUserResponse_QNAME, RegisterNewUserResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUserHistoryResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://model.dais.arkuzmin.ru/", name = "getUserHistoryResponse")
    public JAXBElement<GetUserHistoryResponse> createGetUserHistoryResponse(GetUserHistoryResponse value) {
        return new JAXBElement<GetUserHistoryResponse>(_GetUserHistoryResponse_QNAME, GetUserHistoryResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConfirmApplication }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://model.dais.arkuzmin.ru/", name = "confirmApplication")
    public JAXBElement<ConfirmApplication> createConfirmApplication(ConfirmApplication value) {
        return new JAXBElement<ConfirmApplication>(_ConfirmApplication_QNAME, ConfirmApplication.class, null, value);
    }

}
