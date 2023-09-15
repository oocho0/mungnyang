package com.mungnyang.dto.service;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SelectedCartRoom {

    List<SelectedCartRoomDto> selectedCartRoomList;

    @Getter
    @Setter
    public static class SelectedCartRoomDto{
        private Long accommodationId;
        private Long roomId;
        private Long cartRoomId;
    }
}
