import { Role } from "src/app/login/models/role";
import { SysResourceDefinition } from "./SysResourceDefinition";

export class SysResourceAuth{

    id!:number;
    systemResource!:SysResourceDefinition;
    systemResourceName!:string;
    createAuth!:string;
    readAuth!:string;
    updateAuth!:string;
    deleteAuth!:string;
    queryAuth!:string;
    submitAuth!:string;
    crudqsString!:string;
    othersString!:string;
    fullPrivilegeString!:string;
    visibleToAll!:boolean;
    username!:string;
    role!:Role;
    creationDateTime!:string;
    creationUser!:string;
    lastUpdateDateTime!:string;
    lastUpdateUser!:string;



}