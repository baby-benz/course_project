package ru.cafeteriaitmo.server.controller.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cafeteriaitmo.server.filler.database.OneOrderDataFiller;
import ru.cafeteriaitmo.server.filler.database.OrderDataFiller;
import ru.cafeteriaitmo.server.filler.database.ProductDataFiller;

@Slf4j
@RestController
@RequestMapping("/api/filler")
@RequiredArgsConstructor
public class FillerController {
    private final ProductDataFiller productDataFiller;
    private final OrderDataFiller orderDataFiller;
    private final OneOrderDataFiller oneOrderDataFiller;

    @GetMapping
    public String addTestData() {
        productDataFiller.createDailyMenuExample();
        orderDataFiller.createUsersOrdering();
//        oneOrderDataFiller.createOneOrder();
        return "DataBase now contain some products and orders";
    }

}
