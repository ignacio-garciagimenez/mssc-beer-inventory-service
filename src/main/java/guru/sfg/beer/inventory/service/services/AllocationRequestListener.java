package guru.sfg.beer.inventory.service.services;

import guru.sfg.beer.inventory.service.config.JmsConfig;
import guru.sfg.brewery.model.event.AllocateOrderRequest;
import guru.sfg.brewery.model.event.AllocateOrderResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AllocationRequestListener {

    private final AllocationService allocationService;
    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.ALLOCATE_ORDER_QUEUE)
    public void listenForAllocationRequest(AllocateOrderRequest allocateOrderRequest) {
        log.debug("Processing allocation request for order id: " + allocateOrderRequest.getBeerOrderDto().getId());

        AllocateOrderResult.AllocateOrderResultBuilder builder = AllocateOrderResult.builder();
        builder.beerOrderDto(allocateOrderRequest.getBeerOrderDto());

        try {
            Boolean result = allocationService.allocateOrder(allocateOrderRequest.getBeerOrderDto());
            if (result) {
                builder.pendingInventory(false);
            } else {
                builder.pendingInventory(true);
            }

            builder.allocationError(false);
        } catch (Exception e) {
            log.error("Allocation failed for Order Id: " + allocateOrderRequest.getBeerOrderDto().getId());
            builder.allocationError(true);
        }

        jmsTemplate.convertAndSend(JmsConfig.ALLOCATE_ORDER_RESULT_QUEUE, builder.build());

    }
}
