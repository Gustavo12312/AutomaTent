const express = require('express');
const router = express.Router();
const Dev = require("../models/devModel");


router.get('',  async function (req, res) {
    try {
        console.log("Get Device");
        let result = await Dev.getDev(req.dev.id);
        if (result.status != 200) 
            res.status(result.status).send(result.result);
        let dev = new Dev();
        dev.name = result.result.name;
        res.status(result.status).send(dev);
    } catch (err) {
        console.log(err);
        res.status(500).send(err);
    }
});