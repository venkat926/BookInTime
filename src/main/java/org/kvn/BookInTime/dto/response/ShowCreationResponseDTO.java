package org.kvn.BookInTime.dto.response;

import lombok.*;
import org.kvn.BookInTime.enums.ShowTiming;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowCreationResponseDTO {
    private Date showDate;

    private ShowTiming showTiming;

    private String movieName;

    private String theaterName;

    private Date creationDate;
}
