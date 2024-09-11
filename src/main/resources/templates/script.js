function submit() {
                const total = document.querySelector('total').value;
                const currency = document.querySelector('currency').value;
                const intent = document.querySelector('intent').value;
                const paymentMethod = document.querySelector('payment_method').value;
                const description = document.querySelector('description').value;
debugger;
                fetch('http://localhost:8081/payment/create', {
                    method: 'POST',
                    body: JSON.stringify({
                        total,
                        currency,
                        intent,
                        paymentMethod,
                        description
                    }),
                    headers: {
                        'Content-type': 'application/json; charset=UTF-8'
                    }
                });
            }