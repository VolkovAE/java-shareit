package ru.practicum.shareit;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.request.dto.NewRequestItem;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@ContextConfiguration(classes = ShareItServerApp.class)
@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.NONE)//webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
//@Import({NewRequestItem.class})
public class ShareItServerAppTest {
    private final EntityManager em;

    @Test
    void contextLoads() {
    }

    @Test
    public void add(NewRequestItem requestItem, Long userId) {
        assertEquals(1, 1, "1!=1.");
    }
}
