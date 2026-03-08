document.addEventListener("DOMContentLoaded", function () {
    console.log('order-confirm.js loaded');

    const voucherSelect = document.getElementById("voucherSelect");
    const paymentMethodSelect = document.getElementById("paymentMethodSelect");
    const paymentMethodInput = document.getElementById("paymentMethodInput");
    const shippingMethodSelect = document.getElementById("shippingMethodSelect");
    const shippingMethodInput = document.getElementById("shippingMethodInput");
    const voucherForm = document.getElementById("voucherForm");

    // Set hidden inputs khi trang vừa load
    if (paymentMethodSelect && paymentMethodInput) {
        paymentMethodInput.value = paymentMethodSelect.value;
    } else {
        console.warn('Warning: #paymentMethodSelect or #paymentMethodInput not found');
    }

    if (shippingMethodSelect && shippingMethodInput) {
        shippingMethodInput.value = shippingMethodSelect.value;
    } else {
        console.warn('Warning: #shippingMethodSelect or #shippingMethodInput not found');
    }

    // Payment Method
    if (paymentMethodSelect && paymentMethodInput) {
        paymentMethodSelect.addEventListener("change", function () {
            const selectedPayment = paymentMethodSelect.value;
            paymentMethodInput.value = selectedPayment;

            const addressForm = document.querySelector("#addressListSection form");
            if (addressForm) {
                const paymentInput = addressForm.querySelector("input[name='paymentMethod']");
                if (paymentInput) {
                    paymentInput.value = selectedPayment;
                } else {
                    console.warn('Warning: input[name="paymentMethod"] not found in addressForm');
                }
            }

            if (voucherForm) {
                const voucherPaymentInput = voucherForm.querySelector("#voucherPaymentMethod");
                if (voucherPaymentInput) {
                    voucherPaymentInput.value = selectedPayment;
                } else {
                    console.warn('Warning: #voucherPaymentMethod not found');
                }
            }

            console.log('Payment method updated:', selectedPayment);
        });
    }
    
    // Voucher Select
    if (voucherSelect) {
        voucherSelect.addEventListener("change", function () {
            const selectedVoucherId = voucherSelect.value;

            const addressForm = document.querySelector("#addressListSection form");
            if (addressForm) {
                const voucherInput = addressForm.querySelector("input[name='voucherId']");
                if (voucherInput) {
                    voucherInput.value = selectedVoucherId;
                }
            }

            const orderForm = document.getElementById("orderForm");
            if (orderForm) {
                const voucherInput = orderForm.querySelector("input[name='voucherId']");
                if (voucherInput) {
                    voucherInput.value = selectedVoucherId;
                }
            }

            console.log('Voucher ID updated:', selectedVoucherId);
        });
    } else {
        console.warn('Warning: #voucherSelect not found');
    }
});