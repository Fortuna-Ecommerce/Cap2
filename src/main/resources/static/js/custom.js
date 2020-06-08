(function() {
    "use strict";




        // Create a Stripe client.
        var stripe = Stripe('pk_test_B1PgkqwpndJUHJCdlY0I9leL00c395TbE5');

        // Create an instance of Elements.
        var elements = stripe.elements();

        // Create an instance of the card Element.
        var card = elements.create('card');

        // Add an instance of the card Element into the `card-element` <div>.
        card.mount('#card-element');

        // Handle real-time validation errors from the card Element.
        card.addEventListener('change', function (event) {
            var displayError = document.getElementById('card-errors');
            if (event.error) {
                displayError.textContent = event.error.message;
            } else {
                displayError.textContent = '';
            }
        });

        // Handle form submission.
        var form = document.getElementById('payment-form');
        form.addEventListener('submit', function (event) {
            event.preventDefault();
            // handle payment
            handlePayments();
        });


        //handle card submission
        function handlePayments() {
            stripe.createToken(card).then(function (result) {
                if (result.error) {
                    // Inform the user if there was an error.
                    var errorElement = document.getElementById('card-errors');
                    errorElement.textContent = result.error.message;
                } else {
                    // Send the token to your server.
                    var token = result.token.id;
                    var email = $('#email').val();
                    var total = $('#total').val();
                    $.post(
                        "/charge",
                        {email: email, token: token, total: total},
                        function (data) {
                        }, 'json')
                        .done(function(data){
                            let displayError = document.getElementById('card-errors');
                            let message="";
                            let isError=false;
                            let url="localhost:8080";
                            try{
                                isError=data.error.isError;
                                message=data.error.message;
                                url=data.url;
                                console.log(JSON.stringify(message));
                            }
                            catch(e){
                                isError="true";
                                displayError.textContent="Unknown Error Occurred."+e;
                            }
                            if(isError==="true"){
                                displayError.textContent=message;
                            }
                            else {
                                $("#close-payment-modal").click();
                                window.location.assign(url);
                            }
                        })
                        .fail(function(data){
                            console.log(JSON.stringify(data));
                        })
                }
            });
        }



    // document.getElementById( 'validate' ).addEventListener( 'click', function ( e ) {
    //     validation();
    //     e.preventDefault()
    // } )
    //
    // function validation() {
    //     var ordered = document.querySelectorAll( 'input[id^="units_ordered"]' ),
    //         inventory = document.querySelectorAll( 'input[id^="inventory"]' );
    //
    //     document.getElementById( 'error' ).innerHTML = '';
    //
    //     ordered.forEach( function ( item, index ) {
    //         var ordered_val = Number( item.value ) || 0,
    //             inventory_val = Number( inventory[ index ].value ) || 0;
    //
    //
    //             if ( ordered_val <= inventory_val ) {
    //                 return true
    //             } else {
    //                 document.getElementById( 'error' ).innerHTML += 'Row ' + +( index + 1 ) + ': Ordered units of a product cannot be greater than the number of products present in inventory whose value is ' + inventory_val + '<br/>';
    //                 return false
    //             }
    //
    //     })
    // }


    // document.getElementById( 'addProduct' ).addEventListener( 'click', function ( e ) {
    //     validation();
    //     e.preventDefault()
    // } );

    // function validation() {
    //     var ordered = document.querySelectorAll( 'input[id^="amountOrdered"]' ),
    //         inventory = document.querySelectorAll( 'id="amountOnHand"' );
    //
    //     document.getElementById( 'error' ).innerHTML = '';
    //
    //     // ordered.forEach(
    //         function validating( item ) {
    //         var ordered_val = Number( item.value ),
    //             inventory_val = Number( inventory.value );
    //
    //
    //             if ( ordered_val <= inventory_val ) {
    //                 return true
    //             } else {
    //                 document.getElementById( 'error' ).innerHTML += 'Ordered units of a product cannot be greater' +
    //                     ' than the number of products on hand - ' + inventory_val + ' - Please select a lower' +
    //                     ' quantity<br/>';
    //                 return false
    //             }
    //
    //     }
    //     // )
    // }


})();