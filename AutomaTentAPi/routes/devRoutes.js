const express = require('express');
const router = express.Router();
const Dev = require("../models/devModel");


router.get('/data/:id', async function (req, res) {
    try {
        console.log("Get Device and Value");
        
        // Get device information
        let devResult = await Dev.getDev(req.params.id);
        if (devResult.status !== 200) {
            return res.status(devResult.status).send(devResult.result);
        }
        
        // Get value from data table
        let dataResult = await Dev.getdatafromDev(req.params.id);
        if (dataResult.status !== 200) {
            return res.status(dataResult.status).send(dataResult.result);
        }
        
        // If both device and value are found, construct the response
        let dev = new Dev(devResult.result.id, devResult.result.name);
        let responseData = {
            name: dev.name,
            value: dataResult.result
        };

        res.status(200).send(responseData);
    } catch (err) {
        console.error(err);
        res.status(500).send(err);
    }
});

router.get('/', async function (req, res) {
    try {
        console.log("Get All Devices");
        let result = await Dev.getAllDevs();
        if (result.status != 200)
            return res.status(result.status).send(result.result);
        // If successful, return the array of device names
        res.status(result.status).send(result.result);
    } catch (err) {
        console.log(err);
        res.status(500).send(err);
    }
});

router.put('/data/update/:id', async function (req, res) {
    try {
        console.log("Update Device");
        console.log(`Updating device with ID: ${req.params.id}, New value: ${req.body.value}`);
        let result = await Dev.updateDeviceValue(req.params.id, req.body.value);
        if (result.status != 200)
            return res.status(result.status).send(result.result);
        res.status(200).send({ message: "Device updated successfully." });
    } catch (err) {
        console.log(err);
        res.status(500).send(err);
    }
});


module.exports = router;