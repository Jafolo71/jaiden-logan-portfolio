package contactservice;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ContactTest {

    @Test
    void testContactCreatedSuccessfully() {
        Contact contact = new Contact("12345", "Jaiden", "Logan", "1234567890", "123 Main Street");

        assertEquals("12345", contact.getContactId());
        assertEquals("Jaiden", contact.getFirstName());
        assertEquals("Logan", contact.getLastName());
        assertEquals("1234567890", contact.getPhone());
        assertEquals("123 Main Street", contact.getAddress());
    }

    @Test
    void testContactIdCannotBeNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Contact(null, "Jaiden", "Logan", "1234567890", "123 Main Street");
        });
    }

    @Test
    void testContactIdCannotBeLongerThanTenCharacters() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Contact("12345678901", "Jaiden", "Logan", "1234567890", "123 Main Street");
        });
    }

    @Test
    void testFirstNameCannotBeNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Contact("12345", null, "Logan", "1234567890", "123 Main Street");
        });
    }

    @Test
    void testFirstNameCannotBeLongerThanTenCharacters() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Contact("12345", "LongerName1", "Logan", "1234567890", "123 Main Street");
        });
    }

    @Test
    void testLastNameCannotBeNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Contact("12345", "Jaiden", null, "1234567890", "123 Main Street");
        });
    }

    @Test
    void testLastNameCannotBeLongerThanTenCharacters() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Contact("12345", "Jaiden", "LongerName1", "1234567890", "123 Main Street");
        });
    }

    @Test
    void testPhoneCannotBeNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Contact("12345", "Jaiden", "Logan", null, "123 Main Street");
        });
    }

    @Test
    void testPhoneCannotBeShorterThanTenDigits() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Contact("12345", "Jaiden", "Logan", "12345", "123 Main Street");
        });
    }

    @Test
    void testPhoneCannotBeLongerThanTenDigits() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Contact("12345", "Jaiden", "Logan", "12345678901", "123 Main Street");
        });
    }

    @Test
    void testPhoneMustOnlyUseDigits() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Contact("12345", "Jaiden", "Logan", "12345abcde", "123 Main Street");
        });
    }

    @Test
    void testAddressCannotBeNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Contact("12345", "Jaiden", "Logan", "1234567890", null);
        });
    }

    @Test
    void testAddressCannotBeLongerThanThirtyCharacters() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Contact("12345", "Jaiden", "Logan", "1234567890",
                    "1234567890123456789012345678901");
        });
    }

    @Test
    void testFirstNameCanBeUpdated() {
        Contact contact = new Contact("12345", "Jaiden", "Logan", "1234567890", "123 Main Street");

        contact.setFirstName("John");

        assertEquals("John", contact.getFirstName());
    }

    @Test
    void testLastNameCanBeUpdated() {
        Contact contact = new Contact("12345", "Jaiden", "Logan", "1234567890", "123 Main Street");

        contact.setLastName("Smith");

        assertEquals("Smith", contact.getLastName());
    }

    @Test
    void testPhoneCanBeUpdated() {
        Contact contact = new Contact("12345", "Jaiden", "Logan", "1234567890", "123 Main Street");

        contact.setPhone("0987654321");

        assertEquals("0987654321", contact.getPhone());
    }

    @Test
    void testAddressCanBeUpdated() {
        Contact contact = new Contact("12345", "Jaiden", "Logan", "1234567890", "123 Main Street");

        contact.setAddress("456 Oak Street");

        assertEquals("456 Oak Street", contact.getAddress());
    }
}