function changePage(url,msg,verbose) {
    window.location.pathname = url;
    if (verbose) alert(msg);
}


// It will go to the login page if not authenticated
// Otherwise it will set the window.user with the user profile
async function checkAuthenticated(verbose) {
    try {
        let result = await requestProfile();
        if (result.user.msg =="Invalid authentication!" || result.user.msg =="Please log in.")
            return{successful:true, authenthicated:false}
        else if (!result.successful || result.err) 
            throw err || "Not successful";
        else window.user = result.user;
        return {successful: true,authenthicated:true};
    } catch (err) {
        console.log(err);
        return {err:err};
    }
}

async function logout(){
    let result = requestLogout();
    alert("You have logged out! Going to the initial page.");
    window.location.pathname = "index.html";
}
