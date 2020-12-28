package guru.sfg.brewery.model.event;


import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BeerEvent implements Serializable {


    private BeerDto beerDto;
}
