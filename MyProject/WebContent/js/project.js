$(function() {
	signup = $("#signup").button({
		disabled : true
	});
	$("[name='email']").focusout(validateEmail).blur(validateEmail);
	$("[name='password']").focusout(validatePassword).blur(validatePassword);
	$("[name='rPassword']").focusout(validateRepeatPassword).blur(
			validateRepeatPassword);

});


function isAllValid() {
	var valid = true;
	$("input[name]").each(function(i, e) {
		return valid = valid && (!!e.value);
	});
	$("[id$='_Error']").each(function(i, e) {

		return valid = valid && (!e.innerHTML);
	});
	signup.button({
		disabled : !valid
	});
}

var emailRegex = /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i;
var password;
function validateEmail(e) {
	e = e.target.value;
	var error = "";
	if (e.length == 0) {
		error = "Email is required.";
	} else if (e.length > 32) {
		error = "This field cannot exceed the length of 32.";
	} else if (!emailRegex.test(e)) {
		error = "Invalid Email address.";
	} else {
		isAllValid();
	}

	$("#emailError").html(error);

}

function validatePassword(e) {
	e = e.target.value;
	password = e;
	var error = "";
	if (e.length == 0) {
		error = "Password is required";
	} else if (e.length > 24) {
		error = "This field cannot exceed the length of 24.";
	} else {
		isAllValid();
	}

	$("#passwordError").html(error);

}

function validateRepeatPassword(e) {
	e = e.target.value;
	var error = "";
	if (e != password) {
		error = "Password retyped incorrectly";
		$("[name='rPassword']").focus
	} else {
		isAllValid();
	}
	$("#passwordMatchError").html(error);

}

function readSubscribedItems() {
	var checked;
	if (document.getElementById('check1').checked) {
		checkedGeneral = document.getElementById('r1').value;
	}
	if (document.getElementById('check2').checked) {
		checkedGeneral = document.getElementById('check2').value;
	}
}

$(function() {
	$("#catformat").button();
});
