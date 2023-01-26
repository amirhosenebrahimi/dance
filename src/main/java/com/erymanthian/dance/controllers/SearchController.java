package com.erymanthian.dance.controllers;

import com.erymanthian.dance.entities.Event;
import com.erymanthian.dance.entities.auth.Dancer;
import com.erymanthian.dance.enums.DanceStyle;
import com.erymanthian.dance.enums.PayGrade;
import com.erymanthian.dance.services.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService service;

    @GetMapping("/event")
    public List<Event> searchEvent(Authentication authentication,
                                   @RequestParam(value = "q", required = false) String query,
                                   @RequestParam("p") int page,
                                   @RequestParam("s") int size,
                                   @RequestParam(value = "my", required = false) Boolean myEvents,
                                   @RequestParam(required = false) PayGrade startPayGrade,
                                   @RequestParam(required = false) PayGrade endPayGrade,
                                   @RequestParam(required = false) DanceStyle style,
                                   @RequestParam boolean nearby) {
        return service.searchEvent(authentication, query, page, size, myEvents, startPayGrade, endPayGrade, style, nearby);
    }

    @GetMapping("/talent")
    public List<Dancer> searchTalent(Authentication authentication,
                                     @RequestParam(value = "q", required = false) String query,
                                     @RequestParam("p") int page,
                                     @RequestParam("s") int size,
                                     @RequestParam(required = false) DanceStyle style,
                                     @RequestParam boolean nearby) {
        return service.searchTalent(authentication, query, nearby, page, size);
    }
}
