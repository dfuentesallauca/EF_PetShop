const urlParams = new URLSearchParams(window.location.search);
const status = urlParams.get('status');

if(status === 'deleted'){
	
	Swal.fire({
	  title: "Se eliminó correctamente",
	  icon: "success",
	  showConfirmButton: false,
	  timer: 2000
	});
}

if(status === 'insertd'){
	
	Swal.fire({
	  title: "Se registró correctamente",
	  icon: "success",
	  showConfirmButton: false,
	  timer: 2000
	});
}

if(status === 'updated'){
	
	Swal.fire({
	  title: "Se actualizó correctamente",
	  icon: "success",
	  showConfirmButton: false,
	  timer: 2000
	});
}

