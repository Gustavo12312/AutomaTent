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
