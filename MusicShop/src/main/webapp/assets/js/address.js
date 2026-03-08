
const vietnamProvinces = [
    "An Giang", "Bà Rịa - Vũng Tàu", "Bắc Giang", "Bắc Kạn", "Bạc Liêu", "Bắc Ninh", "Bến Tre", "Bình Định",
    "Bình Dương", "Bình Phước", "Bình Thuận", "Cà Mau", "Cần Thơ", "Cao Bằng", "Đà Nẵng", "Đắk Lắk", "Đắk Nông",
    "Điện Biên", "Đồng Nai", "Đồng Tháp", "Gia Lai", "Hà Giang", "Hà Nam", "Hà Nội", "Hà Tĩnh", "Hải Dương",
    "Hải Phòng", "Hậu Giang", "Hòa Bình", "Hưng Yên", "Khánh Hòa", "Kiên Giang", "Kon Tum", "Lai Châu", "Lâm Đồng",
    "Lạng Sơn", "Lào Cai", "Long An", "Nam Định", "Nghệ An", "Ninh Bình", "Ninh Thuận", "Phú Thọ", "Phú Yên",
    "Quảng Bình", "Quảng Nam", "Quảng Ngãi", "Quảng Ninh", "Quảng Trị", "Sóc Trăng", "Sơn La", "Tây Ninh",
    "Thái Bình", "Thái Nguyên", "Thanh Hóa", "Thừa Thiên Huế", "Tiền Giang", "TP. Hồ Chí Minh", "Trà Vinh",
    "Tuyên Quang", "Vĩnh Long", "Vĩnh Phúc", "Yên Bái"
];

function populateCitySelect(selectId) {
    const select = document.getElementById(selectId);
    if (!select)
        return;

    // Clear old options
    select.innerHTML = '<option value="" disabled selected>-- Select City/Province --</option>';
    vietnamProvinces.forEach(city => {
        const option = document.createElement("option");
        option.value = city;
        option.textContent = city;
        select.appendChild(option);
    });

    // Re-initialize Choices.js if used
    if (typeof Choices !== 'undefined') {
        new Choices(select, {
            searchEnabled: true,
            itemSelectText: '',
            shouldSort: false
        });
    }
}

function openAddressModal(mode, id = '', receiverName = '', receiverPhone = '', street = '', ward = '', district = '', city = '', isDefault = false, style = '1') {
    populateCitySelect('city');

    const modal = document.getElementById('addressModal');
    const formAction = document.getElementById('formAction');
    const addressId = document.getElementById('addressId');
    const receiverNameInput = document.getElementById('receiverName');
    const receiverPhoneInput = document.getElementById('receiverPhone');
    const streetInput = document.getElementById('street');
    const wardInput = document.getElementById('ward');
    const districtInput = document.getElementById('district');
    const citySelect = document.getElementById('city');
    const isDefaultInput = document.getElementById('isDefault');

    const modalTitle = document.getElementById("addressModalLabel");
    modalTitle.textContent = (mode === 'edit') ? "Edit Address" : "Add New Address";

    // Clear errors
    [receiverNameInput, receiverPhoneInput, streetInput, wardInput, districtInput, citySelect].forEach(input => {
        if (input)
            input.classList.remove('is-invalid');
    });
    ["nameError", "phoneError", "streetError", "wardError", "districtError", "cityError"].forEach(id => {
        const error = document.getElementById(id);
        if (error)
            error.textContent = "";
    });

    // Set values
    if (mode === 'edit') {
        formAction.value = 'update';
        addressId.value = id;
        receiverNameInput.value = receiverName;
        receiverPhoneInput.value = receiverPhone;
        streetInput.value = street;
        wardInput.value = ward;
        districtInput.value = district;
        citySelect.value = vietnamProvinces.includes(city) ? city : '';
        isDefaultInput.checked = isDefault === true || isDefault === 'true';

        // Set radio style
        if (style) {
            const styleInput = document.querySelector(`input[name="style"][value="${style}"]`);
            if (styleInput)
                styleInput.checked = true;
        }

    } else {
        formAction.value = 'add';
        addressId.value = '';
        receiverNameInput.value = '';
        receiverPhoneInput.value = '';
        streetInput.value = '';
        wardInput.value = '';
        districtInput.value = '';
        citySelect.value = '';
        isDefaultInput.checked = false;
        document.querySelector('input[name="style"][value="1"]').checked = true;
    }

    new bootstrap.Modal(modal).show();
}
