import React, {useState} from "react";
import {MdAddCircle} from "react-icons/all";
import "./TodoInsert.css";

const TodoInsert = ({onIsertToggle, onInsertTodo}) => {
    const [value, setvalue] = useState("");
    const onChange = (e) => {
        setvalue(e.target.value);
    };

    const onSubmit = (e)=>{
        e.preventDefault();
        onInsertTodo(value);
        setvalue("");
        onIsertToggle();
    }

    return (
        <div>
            <div className="background" onClick={onIsertToggle}></div>
            <form onSubmit={onSubmit}>
                <input placeholder="plese type" value={value} onChange={onChange}></input>
                <button type="submit"><MdAddCircle/></button>
            </form>
    </div>
    );
};

export default TodoInsert;