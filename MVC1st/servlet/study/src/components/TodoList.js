import React from "react";
import TodoItem from "./TodoItem";
import './TodoList.css';

const TodoList = ({todos, onCheckTooggle}) => {
    return(
    <div className="TodoList">
        {todos.map(todo=>(
            <TodoItem todo={todo} key={todo.id} onCheckToggle={onCheckTooggle}/>
        ))}
    </div>
    );
};
export default TodoList;