async function requestAllDevs() {
    try {
        const response = await fetch(`/api/dev/`, 
        {
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
        method: "GET",
        });
        return { successful: response.status == 200};
    } catch (err) {
        // Treat 500 errors here
        console.log(err);
        return {err: err};
    }
    
}
async function requestDev() {
    try {
        const response = await fetch(`/api/dev/:id`, 
        {
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
        method: "GET",
        });
        return { successful: response.status == 200};
    } catch (err) {
        // Treat 500 errors here
        console.log(err);
        return {err: err};
    }
    
}
async function updateDevValue() {
    try {
        const response = await fetch(`/api/dev/update/:id`, 
        {
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
        method: "PUT",
        body: JSON.stringify({ value })
        });
        return { successful: response.status == 200};
    } catch (err) {
        // Treat 500 errors here
        console.log(err);
        return {err: err};
    }
}

