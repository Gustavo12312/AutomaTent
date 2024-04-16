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
async function requestDevandData() {
    try {
        const response = await fetch(`/api/dev/data/:id`, 
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
async function updateDevData() {
    try {
        const response = await fetch(`/api/dev/data/update/:id`, 
        {
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
        method: "PUT",
        body: JSON.stringify({ data: newData})
        });
        return { successful: response.status == 200};
    } catch (err) {
        // Treat 500 errors here
        console.log(err);
        return {err: err};
    }
}

