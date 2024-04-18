const pool = require("../config/database");

function dbDevToDev(dbDev)  {
    let dev = new Dev();
    dev.id = dbDev.dev_id;
    dev.name = dbDev.dev_name;
    dev.value = dbDev.dev_value;
    return dev;
}

class Dev {
    constructor(id, name, value) {
        this.id = id;
        this.name = name;
        this.value = value;
    
    }
    export() {
        let dev =new Dev();
        dev.name = this.name;
        dev.value = this.value;
        return dev; 
    }

    static async getDev(id) {
        try {
            let dbResult = await pool.query("Select * from dev where dev_id=$1", [id]);
            let dbDevs = dbResult.rows;
            if (!dbDevs.length) 
                return { status: 404, result:{msg: "No device found for that id."} } ;
            let dbDev = dbDevs[0];
            return { status: 200, result: 
                new Dev(dbDev.dev_id, dbDev.dev_name, dbDev.dev_value)} ;
        } catch (err) {
            console.log(err);
            return { status: 500, result: err };
        }  
    }

    static async getAllDevs() {
        try {
            let dbResult = await pool.query("select * from dev ");
            let dbDevs = dbResult.rows;
            if (!dbDevs.length) 
                return { status: 404, result: { msg: "No devices found." } };
            let dev = dbDevs.map(dbDev => new Dev(dbDev.dev_id, dbDev.dev_name, dbDev.dev_value));
            return { status: 200, result: dev };
        } catch (err) {
            console.log(err);
            return { status: 500, result: err };
        }
    }


    static async updateDeviceValue(id, newValue) {
        try {
            // Validate the new value
            if (newValue !== 0 && newValue !== 1) {
                return { status: 404, result: { msg: "Invalid value. Value should be either 0 or 1." } };
            }
            
            // Update the device value in the database
            await pool.query("UPDATE dev SET dev_value = $1 WHERE dev_id = $2", [newValue, id]);
    
            return { status: 200, result: { msg: "Device value updated successfully." } };
        } catch (err) {
            console.log(err);
            return { status: 500, result: err };
        }
    }
    
    

}
module.exports = Dev;