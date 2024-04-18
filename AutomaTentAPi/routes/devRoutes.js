const express = require('express');
const router = express.Router();
const Dev = require("../models/devModel");


router.get('/:id', async function (req, res) {
    try {
        console.log("Get Device");
        // Get device information
        let devResult = await Dev.getDev(req.params.id);
        if (devResult.status !== 200) {
            return res.status(devResult.status).send(devResult.result);
        }
        res.status(200).send(devResult.result);
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

router.put('/update/:id', async function (req, res) {
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