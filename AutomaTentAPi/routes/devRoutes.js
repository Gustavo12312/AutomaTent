const express = require('express');
const router = express.Router();
const Dev = require("../models/devModel");


router.get('/:id',  async function (req, res) {
    try {
        console.log("Get Device");
        let result = await Dev.getDev(req.params.id);
        if (result.status != 200) 
            return res.status(result.status).send(result.result);
        let dev = new Dev();
        dev.name = result.result.name;
        res.status(result.status).send(dev);
    } catch (err) {
        console.log(err);
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


module.exports = router;