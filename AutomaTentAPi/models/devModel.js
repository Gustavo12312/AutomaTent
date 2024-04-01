const pool = require("../config/database");

function dbDevToDev(dbDev)  {
    let dev = new Dev();
    dev.id = dbDev.dev_id;
    dev.name = dbDev.dev_name;
    return dev;
}

class Dev {
    constructor(id, name) {
        this.id = id;
        this.name = name;
    
    }
    export() {
        let dev =new Dev();
        dev.name = this.name;
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
                new Dev(dbDev.dev_id, dbDev.dev_name)} ;
        } catch (err) {
            console.log(err);
            return { status: 500, result: err };
        }  
    }
}