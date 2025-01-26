package com.muzkat.reminder.controllers;

import com.muzkat.reminder.model.Remind;
import com.muzkat.reminder.service.RemindService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequestMapping("remind")
@RequiredArgsConstructor
public class RemindController {

    @GetMapping("{title}")
    public ResponseEntity<Remind> findByTitle(@PathVariable String title){
        return ResponseEntity.status(HttpStatus.OK).body(remindService.findRemindByTitle(title));
    }

    @GetMapping("{description}")
    public ResponseEntity<Remind> findByDescription(@PathVariable String description){
        return ResponseEntity.status(HttpStatus.OK).body(remindService.findRemindByDescription(description));
    }


    @GetMapping("{date}")
    public ResponseEntity<Remind> findByDate(@PathVariable LocalDateTime localDateTime){
        return ResponseEntity.status(HttpStatus.OK).body(remindService.findRemindByDate(localDateTime));
    }

    @GetMapping("{time}")
    public ResponseEntity<Remind> findByTime(@PathVariable LocalDateTime localDateTime){
        return ResponseEntity.status(HttpStatus.OK).body(remindService.findRemindByTime(localDateTime));
    }

    @PostMapping
    public ResponseEntity<Remind> createRemind(@RequestBody Remind remind){
        Remind createRemind = remindService.createRemind(remind);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createRemind.getRemindId())
                .toUri();
        return ResponseEntity.created(location).body(createRemind);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Remind> deleteRemind(@PathVariable Long id){
        remindService.deleteRemind(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("{title}")
    public ResponseEntity<Remind> updateRemindByTitle(@PathVariable String title, @RequestBody Remind remind){
        return ResponseEntity.ok().body(remindService.updateRemindByTitle(title, remind));
    }

    @PutMapping("{id}")
    public ResponseEntity<Remind> updateRemindById(@PathVariable Long id, @RequestBody Remind remind){
        return ResponseEntity.ok().body(remindService.updateRemindById(id, remind));
    }

    /**
     * Поле экземпляр RemindService
     */
    private RemindService remindService;
}
