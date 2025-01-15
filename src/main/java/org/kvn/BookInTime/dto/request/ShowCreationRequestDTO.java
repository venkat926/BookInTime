package org.kvn.BookInTime.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.kvn.BookInTime.enums.ShowTiming;
import org.kvn.BookInTime.model.Show;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowCreationRequestDTO {

    @NotNull(message = "showDate is mandatory")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSerialize(using = DateSerializer.class)
    @JsonDeserialize(using = DateDeserializers.DateDeserializer.class)
    private Date showDate;

    @NotBlank(message = "showTiming is mandatory")
    private ShowTiming showTiming;

    @NotNull(message = "movieId is mandatory")
    private Integer movieId;

    @NotNull(message = "theaterId is mandatory")
    private Integer theaterId;

    public Show toShow() {
        return Show.builder()
                .showDate(showDate)
                .showTiming(showTiming)
                .build();
    }
}
