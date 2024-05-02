package com.fisa.woorionebank.common.dummy;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dummy")
@RequiredArgsConstructor
@Tag(name = "더미 API", description = "더미 데이터 insert")
public class DummyController {
    private final DummyService dummyService;

    @GetMapping("")
    public void test() {
        dummyService.initData();
    }
}
