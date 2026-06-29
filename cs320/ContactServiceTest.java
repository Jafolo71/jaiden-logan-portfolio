package contactservice;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ContactServiceTest {

    @Test
    void testAddContact() {
        ContactService service = new ContactService();
        Contact contact = new Contact("12345", "Jaiden", "Logan", "1234567890", "123 Main Street");

        service.addContact(contact);

        assertEquals(1, service.getContactCount());
        assertEquals("Jaiden", service.getContact("12345").getFirstName());
    }

    @Test
    void testCannotAddNullContact() {
        ContactService service = new ContactService();

        assertThrows(IllegalArgumentException.class, () -> {
            service.addContact(null);
        });
    }

    @Test
    void testCannotAddDuplicateContactId() {
        ContactService service = new ContactService();
        Contact contactOne = new Contact("12345", "Jaiden", "Logan", "1234567890", "123 Main Street");
        Contact contactTwo = new Contact("12345", "John", "Smith", "0987654321", "456 Oak Street");

        service.addContact(contactOne);

        assertThrows(IllegalArgumentException.class, () -> {
            service.addContact(contactTwo);
        });
    }

    @Test
    void testDeleteContact() {
        ContactService service = new ContactService();
        Contact contact = new Contact("12345", "Jaiden", "Logan", "1234567890", "123 Main Street");

        service.addContact(contact);
        service.deleteContact("12345");

        assertEquals(0, service.getContactCount());
    }

    @Test
    void testDeleteContactThatDoesNotExist() {
        ContactService service = new ContactService();

        assertThrows(IllegalArgumentException.class, () -> {
            service.deleteContact("99999");
        });
    }

    @Test
    void testUpdateFirstName() {
        ContactService service = new ContactService();
        Contact contact = new Contact("12345", "Jaiden", "Logan", "1234567890", "123 Main Street");

        service.addContact(contact);
        service.updateFirstName("12345", "John");

        assertEquals("John", service.getContact("12345").getFirstName());
    }

    @Test
    void testUpdateLastName() {
        ContactService service = new ContactService();
        Contact contact = new Contact("12345", "Jaiden", "Logan", "1234567890", "123 Main Street");

        service.addContact(contact);
        service.updateLastName("12345", "Smith");

        assertEquals("Smith", service.getContact("12345").getLastName());
    }

    @Test
    void testUpdatePhone() {
        ContactService service = new ContactService();
        Contact contact = new Contact("12345", "Jaiden", "Logan", "1234567890", "123 Main Street");

        service.addContact(contact);
        service.updatePhone("12345", "0987654321");

        assertEquals("0987654321", service.getContact("12345").getPhone());
    }

    @Test
    void testUpdateAddress() {
        ContactService service = new ContactService();
        Contact contact = new Contact("12345", "Jaiden", "Logan", "1234567890", "123 Main Street");

        service.addContact(contact);
        service.updateAddress("12345", "456 Oak Street");

        assertEquals("456 Oak Street", service.getContact("12345").getAddress());
    }

    @Test
    void testUpdateContactThatDoesNotExist() {
        ContactService service = new ContactService();

        assertThrows(IllegalArgumentException.class, () -> {
            service.updateFirstName("99999", "John");
        });
    }

    @Test
    void testInvalidUpdateUsesContactValidation() {
        ContactService service = new ContactService();
        Contact contact = new Contact("12345", "Jaiden", "Logan", "1234567890", "123 Main Street");

        service.addContact(contact);

        assertThrows(IllegalArgumentException.class, () -> {
            service.updatePhone("12345", "123");
        });
    }
}