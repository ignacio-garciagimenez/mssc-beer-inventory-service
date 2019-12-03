package guru.sfg.brewery.model.events;


import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BeerEvent implements Serializable {


    private BeerDto beerDto;
}