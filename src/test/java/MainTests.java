import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationService;
import ru.netology.i18n.LocalizationServiceImpl;
import ru.netology.sender.MessageSender;
import ru.netology.sender.MessageSenderImpl;

import java.util.HashMap;
import java.util.Map;

public class MainTests {
        @Test
        void GeoServiceImplTest() {
            GeoService geoService = new GeoServiceImpl();
            Country testCountry = Country.RUSSIA;
            Country result = geoService.byIp(GeoServiceImpl.MOSCOW_IP).getCountry();
            Assertions.assertEquals(testCountry, result);
        }
    @Test
    void LocalizationServiceImplTest() {
        LocalizationService localizationService = new LocalizationServiceImpl();
        String testText = "Добро пожаловать";
        Assertions.assertEquals(testText, localizationService.locale(Country.RUSSIA));
    }
    @Test
    public void testRussianMessage() {
        GeoService geoService = Mockito.mock(GeoServiceImpl.class);
        Mockito.when(geoService.byIp(GeoServiceImpl.MOSCOW_IP)).thenReturn(new Location(null, Country.RUSSIA, null, 0));
        LocalizationService localizationService = Mockito.mock(LocalizationServiceImpl.class);
        Mockito.when(localizationService.locale(Country.RUSSIA)).thenReturn("Добро пожаловать");
        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);
        Map<String, String> headers = new HashMap<>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, GeoServiceImpl.MOSCOW_IP);
        String testText = "Добро пожаловать";
        String result = messageSender.send(headers);
        Assertions.assertEquals(testText, result);
    }

    @Test
    public void testUsaMessage() {
        GeoService geoService = Mockito.mock(GeoServiceImpl.class);
        Mockito.when(geoService.byIp(GeoServiceImpl.NEW_YORK_IP)).thenReturn(new Location(null, Country.USA, null, 0));
        LocalizationService localizationService = Mockito.mock(LocalizationServiceImpl.class);
        Mockito.when(localizationService.locale(Country.USA)).thenReturn("Welcome");
        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);
        Map<String, String> headers = new HashMap<>();
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, GeoServiceImpl.NEW_YORK_IP);
        String expected = "Welcome";
        String result = messageSender.send(headers);
        Assertions.assertEquals(expected, result);
    }
}