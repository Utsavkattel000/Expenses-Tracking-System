function moveUp(data){
    const inputLabel= document.querySelectorAll('.input-label')
    // console.log(inputLabel)
    inputLabel.forEach(val=>{
        
        if(val.innerHTML === data){
            val.style.bottom = '33px'
        }
              
    })
}



// ----------email validation

//const eValidate = document.getElementById('eValidate');
//const eLabel = document.getElementById('eLabel');

//eValidate.addEventListener('keypress', ()=>{
  //  if(!eValidate.value.match(/^[A-Za-z\._\-0-9]*[@][A-Za-z]*[\.][a-z]{2,4}$/)){
  //      eLabel.innerHTML = "Please Enter Correct Email"
    //    return false
    //}
    //eLabel.innerHTML = ""
    //return true;
//})

// ------------password validation----------

function PasswordValidate() {  

    const pw = document.getElementById("pWord").value;  
    const errShow = document.getElementById("error-show");
    //check empty password field  
    if(pw == "") {  
       errShow.innerHTML = "**Fill the password please!";  
    }else
     
   //minimum password length validation  
    if(pw.length < 8) {  
       errShow.innerHTML = "**Password length must be atleast 8 characters";  
    }else
    
  //maximum length of password validation  
    if(pw.length > 15) {  
       errShow.innerHTML = "**Password length must not exceed 15 characters";  
    }else{
        errShow.style.display = 'none'
    }
}  

// -----------------CpassWord validataion---------

function CPasswordValidate() {  

    const cpw = document.getElementById('CpWord').value;
    const errShow = document.getElementById("error-show");
    //check empty cpassword field 
    errShow.innerHTML =""; 
    errShow.style.display= 'block';
    if(cpw == "") { 
       errShow.innerHTML = "**Fill the password please!";  
    } else
     
   //minimum cpassword length validation  
    if(cpw.length < 8) {  

       errShow.innerHTML = "**Password length must be atleast 8 characters";  
    } else
    
  //maximum length of cpassword validation  
    if(cpw.length > 15) {  

       errShow.innerHTML = "**Password length must not exceed 15 characters";  
    } else{
        errShow.style.display = 'none'
    }
}  



