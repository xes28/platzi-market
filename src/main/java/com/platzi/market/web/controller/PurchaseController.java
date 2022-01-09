package com.platzi.market.web.controller;

import com.platzi.market.domain.Purchase;
import com.platzi.market.domain.service.PurchaseService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/purchases")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @GetMapping("/all")
    @ApiOperation("Get all purchases")
    @ApiResponse(code = 200, message = "OK")
    public ResponseEntity<List<Purchase>> getAll(){
        return new ResponseEntity<>(purchaseService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/client/{clientId}")
    @ApiOperation("Search a purchase with a client ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Purchase not found")
    })
    public ResponseEntity<List<Purchase>> getByClientId(@ApiParam(value="The id of the client", required = true, example = "4546221")@PathVariable(name = "clientId") String clientId){
        return purchaseService.getByClient(clientId)
                .filter(purchases -> !purchases.isEmpty())
                .map(purchase -> new ResponseEntity<>(purchase, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/save")
    @ApiOperation("Save a purchase")
    @ApiResponse(code = 200, message = "CREATED")
    public ResponseEntity<Purchase> save(@RequestBody Purchase purchase){
        return new ResponseEntity<>(purchaseService.save(purchase), HttpStatus.CREATED);
    }
}
