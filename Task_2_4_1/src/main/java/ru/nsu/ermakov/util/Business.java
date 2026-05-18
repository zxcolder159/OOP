package ru.nsu.ermakov.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Business {

    public int countBusinessDays(LocalDate start, LocalDate end, List<LocalDate> holidays) {
        int res = 0;
        for(LocalDate i = start; i.isBefore(end); i = i.plusDays(1)) {
            if(i.getDayOfWeek() == DayOfWeek.SATURDAY || i.getDayOfWeek() == DayOfWeek.SUNDAY
                || holidays.contains(i)) {
                continue;
            }
            res++;
        }
        return res;
    }
    public static void main(String[] args) {
        LocalDate start = LocalDate.of(2026, 5, 11);
        LocalDate end = LocalDate.of(2026, 5, 17);
        List<LocalDate> holidays = new ArrayList<>();
        holidays.add(LocalDate.of(2026, 5, 11));
        Business business = new Business();
        System.out.println(business.countBusinessDays(start, end, holidays));

    }
}
